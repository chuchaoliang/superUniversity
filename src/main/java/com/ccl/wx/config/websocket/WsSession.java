package com.ccl.wx.config.websocket;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.ccl.wx.enums.EnumRedis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket session管理工具类
 *
 * @author 褚超亮
 * @date 2020/5/4 22:26
 */
@Slf4j
@Component
public class WsSession {
    private WsSession() {
    }

    @Resource
    private RedisTemplate redisTemplate;

    private static RedisTemplate redisTemplateCopy;

    @PostConstruct
    public void init() {
        redisTemplateCopy = redisTemplate;
    }

    /**
     * 保存连接 session 的地方
     */
    private final static ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 获取全部的session
     *
     * @return
     */
    public static Map<String, WebSocketSession> getSessionPool() {
        return SESSION_POOL;
    }

    /**
     * 在线人数 +1
     */
    public static void addOnlineCount() {
        redisTemplateCopy.opsForValue().increment(EnumRedis.ONLINE_SUM.getValue());
    }

    /**
     * 在线人数 -1
     */
    public static void subOnlineCount() {
        Long sum = redisTemplateCopy.opsForValue().decrement(EnumRedis.ONLINE_SUM.getValue());
        if (sum <= 0L) {
            redisTemplateCopy.opsForValue().set(EnumRedis.ONLINE_SUM.getValue(), 0);
        }
    }

    /**
     * 添加 session
     *
     * @param key
     */
    public static void add(String key, WebSocketSession session) {
        // 在线人数+1 添加 session
        addOnlineCount();
        // redis中添加
        redisTemplateCopy.opsForHash().put(EnumRedis.ONLINE_USER_INFO.getValue(), key, DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN));
        SESSION_POOL.put(key, session);
    }

    /**
     * 删除 session,会返回删除的 session
     *
     * @param key
     * @return
     */
    public static WebSocketSession remove(String key) {
        // 在线人数-1 删除 session
        subOnlineCount();
        // redis中删除
        redisTemplateCopy.opsForHash().delete(EnumRedis.ONLINE_USER_INFO.getValue(), key);
        return SESSION_POOL.remove(key);
    }

    /**
     * 删除并同步关闭连接
     *
     * @param key
     */
    public static void removeAndClose(String key) {
        WebSocketSession session = remove(key);
        if (session != null) {
            try {
                // 关闭连接
                subOnlineCount();
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得 session
     *
     * @param key
     * @return
     */
    public static WebSocketSession get(String key) {
        // 获得 session
        return SESSION_POOL.get(key);
    }
}
