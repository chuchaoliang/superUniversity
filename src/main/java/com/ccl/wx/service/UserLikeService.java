package com.ccl.wx.service;

import com.ccl.wx.entity.UserInfo;
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

    /**
     * 获取全部点赞用户昵称或者信息
     * userid::userNickname
     *
     * @param diaryId  日志id
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    List<UserInfo> getAllLikeUserNickName(String userId, String circleId, Long diaryId);

    /**
     * 根据点赞用户昵称拼接其昵称
     *
     * @param userInfos
     * @return
     */
    String getAllLikeUserNickName(List<UserInfo> userInfos);

    /**
     * 获取此日记的点赞状态
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @param diaryId  日志id
     * @return
     */
    Boolean judgeDiaryLikeStatus(String userId, String circleId, Long diaryId);
}
