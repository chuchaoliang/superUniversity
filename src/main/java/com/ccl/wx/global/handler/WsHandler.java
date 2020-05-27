package com.ccl.wx.global.handler;

import com.ccl.wx.config.websocket.WsSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author 褚超亮
 * @date 2020/5/4 21:13
 */
@Slf4j
@Component
public class WsHandler implements WebSocketHandler {

    /**
     * 接收发送消息触发
     * 信息处理
     *
     * @param session
     * @param webSocketMessage
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        // TODO 这里进行用户私信处理
        //Object payload = webSocketMessage.getPayload();
        //Object token = session.getAttributes().get("token");
        //System.out.println("接收到：" + token + "发送的消息" + payload);
        //session.sendMessage(new TextMessage("server 发送给 " + token + " 消息 " + payload + " " + LocalDateTime.now().toString()));
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
        } else {
            throw new RuntimeException("用户登录已经失效!token为空哦");
        }
    }

    /**
     * 消息传递出错触发
     *
     * @param session
     * @param exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        throw new RuntimeException("消息发送出错了-->" + session.getAttributes().get("token"));
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
        log.info("用户断开连接：" + token);
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
