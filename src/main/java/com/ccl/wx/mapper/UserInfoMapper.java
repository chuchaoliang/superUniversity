package com.ccl.wx.mapper;

import com.ccl.wx.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/4/3 23:00
 */

@Mapper
public interface UserInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    List<UserInfo> findByAll(UserInfo userInfo);

    /**
     * 获取用户自增键最大值
     * @return
     */
    Integer selectMaxUserId();
}