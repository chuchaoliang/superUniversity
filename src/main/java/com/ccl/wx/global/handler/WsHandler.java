package com.ccl.wx.global.handler;

import com.ccl.wx.config.websocket.WsSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.time.LocalDateTime;

/**
 * @author 褚超亮
 * @date 2020/5/4 21:13
 */
@Slf4j
@Component
public class WsHandler implements WebSocketHandler {
    /**
     * 接收发送消息触发
     *
     * @param session
     * @param webSocketMessage
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        Object payload = webSocketMessage.getPayload();
        Object token = session.getAttributes().get("token");
        System.out.println("接收到：" + token + "发送的消息" + payload);
        session.sendMessage(new TextMessage("server 发送给 " + token + " 消息 " + payload + " " + LocalDateTime.now().toString()));
    }

    /**
     * 连接成功触发
     *
     * @param session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Object token = session.getAttributes().get("token");
        if (token != null) {
            // 用户连接成功，放入在线用户缓存
            WsSession.add(token.toString(), session);
            System.out.println("在线人数：" + WsSession.getOnlineCount());
        } else {
            throw new RuntimeException("用户登录已经失效!");
        }
    }

    /**
     * 消息传递出错触发
     *
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
    }

    /**
     * 断开连接后触发
     *
     * @param session
     * @param status
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Object token = session.getAttributes().get("token");
        System.out.println("用户断开连接：" + token);
        if (token != null) {
            // 用户退出，移除缓存
            WsSession.remove(token.toString());
        }
    }

    /**
     * 是否处理分片消息
     *
     * @return
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}