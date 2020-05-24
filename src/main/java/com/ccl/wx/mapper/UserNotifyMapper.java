package com.ccl.wx.mapper;

import com.ccl.wx.entity.UserNotify;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author  褚超亮
 * @date  2020/5/23 22:23
 */

@Mapper
public interface UserNotifyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserNotify record);

    int insertSelective(UserNotify record);

    UserNotify selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserNotify record);

    int updateByPrimaryKey(UserNotify record);
}