package com.ccl.wx.mapper;

import com.ccl.wx.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/6/4 15:32
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
     * 查询全部用户信息
     *
     * @return
     */
    List<UserInfo> selectAllInfo();

    /**
     * 获取用户自增键最大值
     *
     * @return
     */
    Integer selectMaxUserId();
}