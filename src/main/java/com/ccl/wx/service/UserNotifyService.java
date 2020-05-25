package com.ccl.wx.service;

import com.ccl.wx.entity.UserNotify;
    /**
 * @author  褚超亮
 * @date  2020/5/23 22:23
 */

public interface UserNotifyService{

    int deleteByPrimaryKey(Long id);

    int insert(UserNotify record);

    int insertSelective(UserNotify record);

    UserNotify selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserNotify record);

    int updateByPrimaryKey(UserNotify record);

}
