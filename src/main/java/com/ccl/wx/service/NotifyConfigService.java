package com.ccl.wx.service;

import com.ccl.wx.entity.NotifyConfig;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/5/24 15:58
 */

public interface NotifyConfigService {


    int deleteByPrimaryKey(String id);

    int insert(NotifyConfig record);

    int insertSelective(NotifyConfig record);

    NotifyConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NotifyConfig record);

    int updateByPrimaryKey(NotifyConfig record);

    /**
     * 判断用户的消息是否提醒
     *
     * @param type
     * @param userId
     * @return true 提醒 false不提醒
     */
    boolean judgeMessageRemind(Integer type, String userId);

    /**
     * 判断用户消息是否持久化到数据库
     *
     * @param type
     * @param userId
     * @return true 持久化 false不持久化
     */
    boolean judgeMessagePersistence(Integer type, String userId);

    /**
     * 获取消息操作类型
     *
     * @param type
     * @param userId
     * @return
     */
    int getMessageOperationType(Integer type, String userId);

    /**
     * 添加用户配置信息（用户开启提醒）
     *
     * @param configList 配置列表
     * @param userId     用户id
     * @return
     */
    String addUserNotifyConfig(List<Integer> configList, String userId);

    /**
     * 删除用户配置信息（用户关闭提醒但是持久化，或者关闭提醒，并且不持久化）
     *
     * @param configList 配置列表
     * @param userId     用户id
     * @param type       操作类型
     * @return
     */
    String delUserNotifyConfig(List<Integer> configList, String userId, Integer type);
}
