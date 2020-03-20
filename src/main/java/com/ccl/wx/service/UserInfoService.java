package com.ccl.wx.service;

import com.ccl.wx.entity.UserInfo;

/**
 * @author 褚超亮
 * @date 2020/2/29 15:36
 */
public interface UserInfoService {

    int deleteByPrimaryKey(String id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
}
