package com.ccl.wx.mapper;

import com.ccl.wx.entity.SystemUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author  褚超亮
 * @date  2020/6/3 22:55
 */

@Mapper
public interface SystemUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemUser record);

    int insertSelective(SystemUser record);

    SystemUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemUser record);

    int updateByPrimaryKey(SystemUser record);
}