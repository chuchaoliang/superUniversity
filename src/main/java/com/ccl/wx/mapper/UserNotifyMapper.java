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
}