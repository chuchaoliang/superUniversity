package com.ccl.wx.mapper;

import com.ccl.wx.entity.UserNotify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/6/2 22:24
 */

@Mapper
public interface UserNotifyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserNotify record);

    int insertSelective(UserNotify record);

    UserNotify selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserNotify record);

    int updateByPrimaryKey(UserNotify record);

    /**
     * 获取用户通知信息
     *
     * @param sendUserId   发送人id
     * @param targetUserId 目标人id
     * @param action       消息类型
     * @param delete       是否删除
     * @return
     */
    List<UserNotify> selectUserNotifyInfo(@Param("sendUserId") String sendUserId, @Param("targetUserId") String targetUserId,
                                          @Param("action") Integer action, @Param("delete") Integer delete);

    /**
     * 根据通知位置查找消息信息
     *
     * @param userId   用户id
     * @param location 消息位置
     * @param start    开始数据
     * @param number   要查找的数据量
     * @return
     */
    List<UserNotify> selectUserNotifyByNotifyLocation(@Param("userId") String userId, @Param("location") int location,
                                                      @Param("start") int start, @Param("number") int number);

    /**
     * 根据条件查找用户的消息数量
     *
     * @param userId
     * @param location
     * @param read
     * @param delete
     * @return
     */
    Integer getNotifyAllNumber(@Param("userId") String userId, @Param("location") Integer location,
                               @Param("read") Integer read, @Param("delete") Integer delete);
}