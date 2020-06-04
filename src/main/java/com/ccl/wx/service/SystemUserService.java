package com.ccl.wx.service;

import com.ccl.wx.entity.SystemUser;
    /**
 * @author  褚超亮
 * @date  2020/6/3 22:55
 */

public interface SystemUserService{


    int deleteByPrimaryKey(Integer id);

    int insert(SystemUser record);

    int insertSelective(SystemUser record);

    SystemUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemUser record);

    int updateByPrimaryKey(SystemUser record);

}
