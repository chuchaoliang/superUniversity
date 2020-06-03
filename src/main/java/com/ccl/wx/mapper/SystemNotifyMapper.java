package com.ccl.wx.mapper;

import com.ccl.wx.entity.SystemNotify;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author  褚超亮
 * @date  2020/6/3 22:28
 */

@Mapper
public interface SystemNotifyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemNotify record);

    int insertSelective(SystemNotify record);

    SystemNotify selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemNotify record);

    int updateByPrimaryKey(SystemNotify record);
}