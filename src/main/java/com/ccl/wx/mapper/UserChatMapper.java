package com.ccl.wx.mapper;

import com.ccl.wx.entity.UserChat;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author  褚超亮
 * @date  2020/6/1 17:11
 */

@Mapper
public interface UserChatMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserChat record);

    int insertSelective(UserChat record);

    UserChat selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserChat record);

    int updateByPrimaryKey(UserChat record);
}