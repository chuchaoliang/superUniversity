package com.ccl.wx.mapper;

import com.ccl.wx.entity.UserInfo;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/5 12:13
 */

public interface UserInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    List<UserInfo> findByAll(UserInfo userInfo);

    /**
     * 根据openid查询用户信息
     *
     * @param openid
     * @return
     */
    UserInfo findByOpenid(@Param("openid") String openid);
}