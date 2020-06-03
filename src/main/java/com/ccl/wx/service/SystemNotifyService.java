package com.ccl.wx.service;

import com.ccl.wx.entity.SystemNotify;
    /**
 * @author  褚超亮
 * @date  2020/6/3 22:28
 */

public interface SystemNotifyService{


    int deleteByPrimaryKey(Integer id);

    int insert(SystemNotify record);

    int insertSelective(SystemNotify record);

    SystemNotify selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemNotify record);

    int updateByPrimaryKey(SystemNotify record);

}
