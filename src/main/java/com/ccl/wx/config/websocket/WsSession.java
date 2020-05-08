package com.ccl.wx.config.websocket;

import lombok.extern.slf4j.Slf4j;
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
public class WsSession {
    /**
     * 保存连接 session 的地方
     */
    private final static ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 记录当前在线人数
     */
    private final static AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    /**
     * 获取全部的session
     *
     * @return
     */
    public static Map<String, WebSocketSession> getSessionPool() {
        return SESSION_POOL;
    }

    /**
     * 线程安全的统计在线人数
     *
     * @return
     */
    public static int getOnlineCount() {
        return ONLINE_COUNT.get();
    }

    /**
     * 在线人数 +1
     */
    public static void addOnlineCount() {
        ONLINE_COUNT.incrementAndGet();
    }

    /**
     * 在线人数 -1
     */
    public static void subOnlineCount() {
        ONLINE_COUNT.decrementAndGet();
    }

    /**
     * 添加 session
     *
     * @param key
     */
    public static void add(String key, WebSocketSession session) {
        // 添加 session
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
        // 删除 session
        subOnlineCount();
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
