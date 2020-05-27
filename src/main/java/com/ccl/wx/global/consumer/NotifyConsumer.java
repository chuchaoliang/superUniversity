package com.ccl.wx.global.consumer;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.config.properties.RabbitMQData;
import com.ccl.wx.config.websocket.WsSession;
import com.ccl.wx.entity.UserNotify;
import com.ccl.wx.enums.common.EnumCommon;
import com.ccl.wx.enums.notify.EnumNotifyType;
import com.ccl.wx.pojo.NotifyTemplate;
import com.ccl.wx.service.NotifyConfigService;
import com.ccl.wx.service.UserNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author 褚超亮
 * @date 2020/5/23 21:53
 */
@Component
@Slf4j
public class NotifyConsumer {
    @Resource
    private NotifyConfigService notifyConfigService;

    @Resource
    private UserNotifyService userNotifyService;

    /**
     * 监听点赞消息
     */
    @RabbitListener(queues = RabbitMQData.LIKE)
    public void like(String msg) throws IOException {
        UserNotify userNotify = JSON.parseObject(msg, UserNotify.class);
        userNotify.setRead((byte) EnumCommon.HAVE_READ.getData());
        // 插入用户消息数据
        userNotifyService.insertSelective(userNotify);
        // 判断用户是否消息提醒
        String targetId = userNotify.getTargetId();
        if (notifyConfigService.judgeMessageRemind(EnumNotifyType.DIARY_LIKE.getValue(), targetId)) {
            // 进行消息提醒，判断用户是否在线
            if (WsSession.judgeUserOnline(targetId)) {
                // 用户在线进行消息提醒
                NotifyTemplate notifyTemplate = new NotifyTemplate();
                notifyTemplate.setMessageType(EnumNotifyType.DIARY_LIKE.getValue());
                notifyTemplate.setMessageLocation(EnumNotifyType.DIARY_LIKE.getLocation());
                WebSocketSession session = WsSession.get(targetId);
                session.sendMessage(new TextMessage(JSON.toJSONString(notifyTemplate)));
            }
        }
    }
}
