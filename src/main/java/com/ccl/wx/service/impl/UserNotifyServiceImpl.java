package com.ccl.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.common.notify.IUserNotify;
import com.ccl.wx.config.websocket.WsSession;
import com.ccl.wx.entity.UserNotify;
import com.ccl.wx.enums.common.EnumCommon;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.enums.notify.EnumNotifyType;
import com.ccl.wx.mapper.UserNotifyMapper;
import com.ccl.wx.pojo.NotifyTemplate;
import com.ccl.wx.service.NotifyConfigService;
import com.ccl.wx.service.UserNotifyService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/5/23 22:23
 */

@Service
public class UserNotifyServiceImpl implements UserNotifyService {

    @Resource
    private UserNotifyMapper userNotifyMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private NotifyConfigService notifyConfigService;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userNotifyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserNotify record) {
        return userNotifyMapper.insert(record);
    }

    @Override
    public int insertSelective(UserNotify record) {
        return userNotifyMapper.insertSelective(record);
    }

    @Override
    public UserNotify selectByPrimaryKey(Long id) {
        return userNotifyMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserNotify record) {
        return userNotifyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserNotify record) {
        return userNotifyMapper.updateByPrimaryKey(record);
    }

    @Override
    public String userMessageNotify(IUserNotify userNotifyType, String sendUserId, List<String> targetUserIdList,
                                    Integer resourceId) {
        Integer notifyType = userNotifyType.getNotifyType();
        for (String targetUserId : targetUserIdList) {
            if (notifyConfigService.judgeMessagePersistence(notifyType, targetUserId) && !sendUserId.equals(targetUserId)) {
                // 保存数据到mysql通知表中
                UserNotify userNotify = new UserNotify();
                // 设置目标用户id
                userNotify.setTargetId(targetUserId);
                // 设置发送者用户id
                userNotify.setSenderId(sendUserId);
                // 设置资源类型
                userNotify.setResourceType(userNotifyType.getResourceType().byteValue());
                // 设置动作类型
                userNotify.setAction(notifyType.byteValue());
                // 设置资源id
                userNotify.setResourceId(resourceId);
                // 设置消息所在位置
                userNotify.setLocation(userNotifyType.getNotifyLocation().byteValue());
                // 将数据发送到rabbitmq中
                rabbitTemplate.convertAndSend(EnumNotifyType.EXCHANGE_NAME, userNotifyType.getQueue(), JSON.toJSONString(userNotify));
                return EnumResultStatus.SUCCESS.getValue();
            }
        }
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public String userMessageDispose(String message) throws IOException {
        UserNotify userNotify = JSON.parseObject(message, UserNotify.class);
        // 判断用户是否消息提醒
        String targetId = userNotify.getTargetId();
        // 获取通知类型
        int notifyType = userNotify.getAction();
        boolean judgeMessageRemind = notifyConfigService.judgeMessageRemind(notifyType, targetId);
        // 判断用户是否设置已读或者未读，如果用户设置不提醒则已读，否则未读
        userNotify.setRead(judgeMessageRemind ? (byte) EnumCommon.NOT_READ.getData() : (byte) EnumCommon.HAVE_READ.getData());
        // 插入用户消息数据
        insertSelective(userNotify);
        if (judgeMessageRemind) {
            // 进行消息提醒，判断用户是否在线
            if (WsSession.judgeUserOnline(targetId)) {
                // 用户在线进行消息提醒
                NotifyTemplate notifyTemplate = new NotifyTemplate();
                // 设置消息类型
                notifyTemplate.setMessageType(notifyType);
                // 消息所在位置
                notifyTemplate.setMessageLocation(userNotify.getLocation());
                // 获取用户的session对象
                WebSocketSession session = WsSession.get(targetId);
                // 发送提醒
                session.sendMessage(new TextMessage(JSON.toJSONString(notifyTemplate)));
            }
            return EnumResultStatus.SUCCESS.getValue();
        }
        return EnumResultStatus.FAIL.getValue();
    }
}
