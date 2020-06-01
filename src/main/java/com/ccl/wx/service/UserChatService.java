package com.ccl.wx.service;

import com.ccl.wx.entity.UserChat;

/**
 * @author 褚超亮
 * @date 2020/6/1 17:11
 */

public interface UserChatService {

    int deleteByPrimaryKey(Long id);

    int insert(UserChat record);

    int insertSelective(UserChat record);

    UserChat selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserChat record);

    int updateByPrimaryKey(UserChat record);
}
