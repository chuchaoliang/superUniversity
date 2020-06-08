package com.ccl.wx.mapper;

import com.ccl.wx.entity.NotifyContent;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 褚超亮
 * @date 2020/6/8 16:57
 */

@Mapper
public interface NotifyContentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NotifyContent record);

    int insertSelective(NotifyContent record);

    NotifyContent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NotifyContent record);

    int updateByPrimaryKey(NotifyContent record);
}