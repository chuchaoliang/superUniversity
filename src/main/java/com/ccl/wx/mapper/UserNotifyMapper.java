package com.ccl.wx.mapper;

import com.ccl.wx.entity.UserNotify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 褚超亮
 * @date 2020/5/23 22:23
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
     * @param sendUserId   发送人id
     * @param targetUserId 目标人id
     * @param action       消息类型
     * @param delete       是否删除
     * @return
     */
    UserNotify selectUserNotifyInfo(@Param("sendUserId") String sendUserId, @Param("targetUserId") String targetUserId,
                                    @Param("action") Integer action, @Param("delete") Integer delete);
}