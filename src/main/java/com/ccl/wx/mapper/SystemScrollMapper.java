package com.ccl.wx.mapper;

import com.ccl.wx.entity.SystemScroll;

/**
 * @author  褚超亮
 * @date  2020/2/14 11:28
 */

public interface SystemScrollMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemScroll record);

    int insertSelective(SystemScroll record);

    SystemScroll selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemScroll record);

    int updateByPrimaryKey(SystemScroll record);
}