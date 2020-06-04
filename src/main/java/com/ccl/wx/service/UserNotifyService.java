package com.ccl.wx.service;

import com.ccl.wx.common.notify.IUserNotify;
import com.ccl.wx.entity.UserNotify;

import java.io.IOException;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/5/23 22:23
 */

public interface UserNotifyService {

    int deleteByPrimaryKey(Long id);

    int insert(UserNotify record);

    int insertSelective(UserNotify record);

    UserNotify selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserNotify record);

    int updateByPrimaryKey(UserNotify record);

    /**
     * 处理用户的消息初步处理
     *
     * @param userNotifyType   消息类型
     * @param sendUserId       发送人用户id
     * @param targetUserIdList 目标人用户id
     * @param resourceId       资源id
     * @return "success":发送信息 "fail":未发送信息
     */
    String userMessageNotify(IUserNotify userNotifyType, String sendUserId, List<String> targetUserIdList, Integer resourceId);

    /**
     * 系统通知方法（只提供给系统通知，其他无效）
     *
     * @param userNotifyType
     * @param sendUserId
     * @param targetUserIdList
     * @param resourceId
     * @return
     */
    String systemMessageNotify(IUserNotify userNotifyType, String sendUserId, List<String> targetUserIdList, Integer resourceId);

    /**
     * 用户的消息推送处理
     *
     * @param message 消息内容json字符串
     * @return "success":发送提醒 "fail":不发送提醒
     * @throws IOException
     */
    String userMessageDispose(String message) throws IOException;

    /**
     * 系统消息处理
     *
     * @param message
     * @return
     * @throws IOException
     */
    String systemMessageDispose(String message) throws IOException;

    /**
     * 用户聊天消息处理
     *
     * @param message
     * @return
     */
    String userChatMessageDispose(String message);
}

