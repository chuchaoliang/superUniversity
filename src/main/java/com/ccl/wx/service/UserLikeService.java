package com.ccl.wx.service;

import com.ccl.wx.entity.UserLike;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/25 19:33
 */

public interface UserLikeService {


    int deleteByPrimaryKey(Long id);

    int insert(UserLike record);

    int insertSelective(UserLike record);

    UserLike selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserLike record);

    int updateByPrimaryKey(UserLike record);

    UserLike selectByTypeId(Long typeId);

    void concatLikeUserId(Long typeId, String userId);

    List<Long> selectIdByTypeId(Long typeId);
}
