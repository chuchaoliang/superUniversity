package com.ccl.wx.global.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.config.websocket.WsSession;
import com.ccl.wx.enums.notify.EnumNotifyType;
import com.ccl.wx.enums.system.EnumMessageType;
import com.ccl.wx.util.CclUtil;
import com.ccl.wx.util.ResponseMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import javax.annotation.Resource;

/**
 * @author 褚超亮
 * @date 2020/5/4 21:13
 */
@Slf4j
@Component
public class WsHandler implements WebSocketHandler {

    @Resource
    private RabbitTemplate rabbitTemplate;

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
        // TODO 这里进行用户私信处理、或者其他消息，这里应该设置一个消息类型，私信消息要添加一个id
        String message = (String) webSocketMessage.getPayload();
        Object token = session.getAttributes().get("token");
        if (CclUtil.isJson(message)) {
            // rabbitmq
            JSONObject messageJson = JSON.parseObject(message);
            String mtype = messageJson.get("mtype").toString();
            if (EnumMessageType.USER_CHAT_TYPE.getValue().equals(mtype)) {
                // 用户聊天信息
                rabbitTemplate.convertAndSend(EnumNotifyType.USER_CHAT.getQueue(), message);
            } else if (EnumMessageType.TEST_CONNECTION.getValue().equals(mtype)) {
                // 测试用户连接是否成功
                WebSocketSession userSession = WsSession.get((String) token);
                userSession.sendMessage(new TextMessage(JSON.toJSONString(ResponseMsgUtil.success(EnumResultCode.SUCCESS))));
            }
        }
        log.info("收到：" + token + "发送的消息" + message);
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
