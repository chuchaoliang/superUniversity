package com.ccl.wx.service;

import com.ccl.wx.entity.NotifyContent;
    /**
 * @author  褚超亮
 * @date  2020/6/3 22:29
 */

public interface NotifyContentService{


    int deleteByPrimaryKey(Integer id);

    int insert(NotifyContent record);

    int insertSelective(NotifyContent record);

    NotifyContent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NotifyContent record);

    int updateByPrimaryKey(NotifyContent record);

}
