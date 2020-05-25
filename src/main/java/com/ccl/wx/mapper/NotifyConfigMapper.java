package com.ccl.wx.mapper;

import com.ccl.wx.entity.NotifyConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author  褚超亮
 * @date  2020/5/24 15:58
 */

@Mapper
public interface NotifyConfigMapper {
    int deleteByPrimaryKey(String id);

    int insert(NotifyConfig record);

    int insertSelective(NotifyConfig record);

    NotifyConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NotifyConfig record);

    int updateByPrimaryKey(NotifyConfig record);
}