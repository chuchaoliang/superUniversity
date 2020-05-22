package com.ccl.wx.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * 连接总人数
     */
    private final static AtomicInteger ONLINE_SUM = new AtomicInteger();

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
        ONLINE_SUM.getAndIncrement();
    }

    /**
     * 在线人数 -1
     */
    public static void subOnlineCount() {
        ONLINE_SUM.getAndDecrement();
    }

    /**
     * 获取连接总人数
     *
     * @return
     */
    public static Integer getOnlineSum() {
        return ONLINE_SUM.get();
    }

    /**
     * 添加 session
     *
     * @param key
     */
    public static void add(String key, WebSocketSession session) {
        addOnlineCount();
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
        return removeAndClose(key);
    }

    /**
     * 删除并同步关闭连接
     *
     * @param key
     */
    public static WebSocketSession removeAndClose(String key) {
        WebSocketSession session = SESSION_POOL.remove(key);
        if (session != null) {
            try {
                // 关闭连接
                subOnlineCount();
                session.close();
                return session;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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
