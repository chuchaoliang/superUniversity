package com.ccl.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.common.notify.IUserNotify;
import com.ccl.wx.config.websocket.WsSession;
import com.ccl.wx.entity.UserChat;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.entity.UserNotify;
import com.ccl.wx.enums.common.EnumCommon;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.enums.notify.EnumNotifyType;
import com.ccl.wx.enums.notify.EnumNotifyUserType;
import com.ccl.wx.mapper.UserNotifyMapper;
import com.ccl.wx.pojo.NotifyTemplate;
import com.ccl.wx.service.NotifyConfigService;
import com.ccl.wx.service.UserChatService;
import com.ccl.wx.service.UserInfoService;
import com.ccl.wx.service.UserNotifyService;
import com.ccl.wx.util.CclUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author 褚超亮
 * @date 2020/5/23 22:23
 */

@Service
@Slf4j
public class UserNotifyServiceImpl implements UserNotifyService {

    @Resource
    private UserNotifyMapper userNotifyMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private NotifyConfigService notifyConfigService;
    @Resource
    private UserChatService userChatService;
    @Resource
    private UserInfoService userInfoService;

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
        if (targetUserIdList.isEmpty() || StringUtils.isEmpty(sendUserId) || StringUtils.isEmpty(resourceId)) {
            return EnumResultStatus.FAIL.getValue();
        }
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
            }
        }
        return EnumResultStatus.SUCCESS.getValue();
    }

    @Override
    public String systemMessageNotify(IUserNotify userNotifyType, String sendUserId, List<String> targetUserIdList,
                                      Integer resourceId) {
        // 目标用户为空，并且这个消息必须为系统通知
        if ((targetUserIdList == null || targetUserIdList.isEmpty()) && userNotifyType.getNotifyType().equals(EnumNotifyType.SYSTEM_NOTICE.getNotifyType())) {
            // 发送给全部用户
            targetUserIdList = new ArrayList<>();
            targetUserIdList.add(EnumCommon.SYSTEM_NOTIFY.getValue());
        }
        String result = userMessageNotify(userNotifyType, sendUserId, targetUserIdList, resourceId);
        String success = EnumResultStatus.SUCCESS.getValue();
        if (result.equals(success)) {
            return success;
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
        // 发送消息到用户
        sendMessageToOnlineUser(userNotify, notifyType, targetId);
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public String systemMessageDispose(String message) throws IOException {
        UserNotify userNotify = JSON.parseObject(message, UserNotify.class);
        // 判断用户是否消息提醒
        String targetId = userNotify.getTargetId();
        // 获取通知类型
        int notifyType = userNotify.getAction();
        if (EnumCommon.SYSTEM_NOTIFY.getValue().equals(targetId)) {
            // 获取全部在线用户，通知全部用户
            Set<String> userIds = WsSession.getSessionPool().keySet();
            for (String userId : userIds) {
                // 判断用户是否持久化数据 TODO 系统消息必须设置持久化，无需设置
                //if (notifyConfigService.judgeMessagePersistence(notifyType, userId)) {
                // 发送消息到用户
                userNotify.setTargetId(userId);
                userNotify.setUserType((byte) EnumNotifyUserType.SYSTEM_USER.getValue());
                sendMessageToOnlineUser(userNotify, notifyType, userId);
                //}
            }
        } else {
            return userMessageDispose(message);
        }
        return EnumResultStatus.FAIL.getValue();
    }

    private boolean sendMessageToOnlineUser(UserNotify userNotify, int notifyType, String userId) throws IOException {
        // 判断用户是否设置消息提醒
        boolean judgeMessageRemind = notifyConfigService.judgeMessageRemind(notifyType, userId);
        // 判断用户是否设置已读或者未读，如果用户设置不提醒则已读，否则未读
        userNotify.setRead(judgeMessageRemind ? (byte) EnumCommon.NOT_READ.getData() : (byte) EnumCommon.HAVE_READ.getData());
        int i = insertSelective(userNotify);
        if (i != 0) {
            if (judgeMessageRemind) {
                // 进行消息提醒，判断用户是否在线
                if (WsSession.judgeUserOnline(userId)) {
                    // 用户在线进行消息提醒
                    NotifyTemplate notifyTemplate = new NotifyTemplate();
                    // 设置消息类型
                    notifyTemplate.setMessageType(notifyType);
                    // 消息所在位置
                    notifyTemplate.setMessageLocation(userNotify.getLocation());
                    // 获取用户的session对象
                    WebSocketSession session = WsSession.get(userId);
                    // 发送提醒
                    session.sendMessage(new TextMessage(JSON.toJSONString(notifyTemplate)));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String userChatMessageDispose(String message) {
        if (!CclUtil.isJson(message)) {
            return EnumResultStatus.FAIL.getValue();
        }
        UserChat userChat = JSON.parseObject(message, UserChat.class);
        // 发送者用户id
        String sendId = userChat.getSendId();
        // 目标人用户id
        String targetId = userChat.getTargetId();
        // 聊天内容
        String content = userChat.getContent();
        if (!StringUtils.isEmpty(sendId) && !StringUtils.isEmpty(targetId) && !StringUtils.isEmpty(content)) {
            if (sendId.equals(targetId)) {
                log.error("前端传输数据错误？？？接受者和发送者相同！！" + sendId + "::" + targetId);
                return EnumResultStatus.FAIL.getValue();
            }
            // 判断用户消息是否持久化
            boolean judgeMessagePersistence = notifyConfigService.judgeMessagePersistence(EnumNotifyType.USER_CHAT.getResourceType(), targetId);
            if (judgeMessagePersistence) {
                int i = userChatService.insertSelective(userChat);
                if (i != 0) {
                    // 通知用户
                    boolean judgeMessageRemind = notifyConfigService.judgeMessageRemind(EnumNotifyType.USER_CHAT.getResourceType(), targetId);
                    List<UserNotify> userNotifies = userNotifyMapper.selectUserNotifyInfo(sendId, targetId, EnumNotifyType.USER_CHAT.getNotifyType(),
                            EnumCommon.NOT_DELETE.getData());
                    if (userNotifies.isEmpty()) {
                        // 第一次聊天 插入消息
                        UserNotify notify = new UserNotify();
                        // 设置发送人用户id
                        notify.setSenderId(sendId);
                        // 设置目标人用户id
                        notify.setTargetId(targetId);
                        // 设置是否已读
                        notify.setRead(judgeMessageRemind ? (byte) EnumCommon.NOT_READ.getData() : (byte) EnumCommon.HAVE_READ.getData());
                        // 设置资源类型
                        notify.setResourceType(EnumNotifyType.USER_CHAT.getResourceType().byteValue());
                        // 设置资源位置
                        notify.setLocation(EnumNotifyType.USER_CHAT.getNotifyLocation().byteValue());
                        // 设置资源id
                        notify.setResourceId(userChat.getId().intValue());
                        // 设置消息类型
                        notify.setAction(EnumNotifyType.USER_CHAT.getNotifyType().byteValue());
                        int i1 = userNotifyMapper.insertSelective(notify);
                        sendUserChatMessage(targetId, sendId, userChat.getContent(), i1, judgeMessageRemind, userChat.getId().intValue());
                    } else {
                        // 存在过聊天，更新数据
                        if (userNotifies.size() == 1) {
                            UserNotify userNotify = userNotifies.get(0);
                            userNotify.setRead(judgeMessageRemind ? (byte) EnumCommon.NOT_READ.getData() : (byte) EnumCommon.HAVE_READ.getData());
                            userNotify.setResourceId(userChat.getId().intValue());
                            int i1 = updateByPrimaryKeySelective(userNotify);
                            sendUserChatMessage(targetId, sendId, userChat.getContent(), i1, judgeMessageRemind, userChat.getId().intValue());
                        } else {
                            log.error("用户聊天出现错误！！！----》" + "sendId->" + sendId + "targetId->" + targetId);
                        }
                    }
                }
            }
            return EnumResultStatus.SUCCESS.getValue();
        }
        return EnumResultStatus.FAIL.getValue();
    }

    /**
     * 用户发送消息
     *
     * @param targetUserId 目标用户id
     * @param senderUserId
     * @param content      消息内容
     * @param i            插入或者更新返回值
     * @param remind       是否在线
     * @param id
     * @throws IOException
     */
    private void sendUserChatMessage(String targetUserId, String senderUserId, String content, int i, boolean remind, Integer id) {
        if (i != 0) {
            if (WsSession.judgeUserOnline(targetUserId)) {
                try {
                    // 查找发送人信息
                    UserInfo userInfo = userInfoService.selectByPrimaryKey(senderUserId);
                    if (userInfo != null) {
                        // 发送消息
                        NotifyTemplate notifyTemplate = new NotifyTemplate();
                        // 设置消息类型
                        notifyTemplate.setMessageType(EnumNotifyType.USER_CHAT.getNotifyType());
                        // 设置消息所在位置
                        notifyTemplate.setMessageLocation(EnumNotifyType.USER_CHAT.getNotifyLocation());
                        // 设置消息内容
                        notifyTemplate.setMessageContent(content);
                        // 设置是否提醒
                        notifyTemplate.setRemind(remind);
                        // 设置发送人用户id
                        notifyTemplate.setSenderUserId(senderUserId);
                        // 设置发送人用户昵称
                        notifyTemplate.setNickname(userInfo.getNickname());
                        // 设置发送人用户头像地址
                        notifyTemplate.setHeadPortrait(userInfo.getAvatarurl());
                        // 设置消息id
                        notifyTemplate.setId(id);
                        WebSocketSession session = WsSession.get(targetUserId);
                        // 发送消息
                        session.sendMessage(new TextMessage(JSON.toJSONString(notifyTemplate)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

