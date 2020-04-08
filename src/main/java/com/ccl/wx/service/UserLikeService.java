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

    /**
     * 保存点赞 点赞状态 1 取消点赞 0
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @param diaryId  日志id
     * @return
     */
    String saveLikeRedis(String userId, String circleId, String diaryId);

    /**
     * 取消点赞
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @param diaryId  日志id
     * @return
     */
    String unLikeFromRedis(String userId, String circleId, String diaryId);

    /**
     * 删除点赞数据
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @param diaryId  日志id
     * @return
     */
    String deleteLikeFromRedis(String userId, String circleId, String diaryId);

    /**
     * 日志点赞数+1
     *
     * @param diaryId 日志id
     * @return
     */
    String incrementLikedCount(String diaryId);

    /**
     * 日志点赞数-1
     *
     * @param diaryId 日志id
     * @return
     */
    String decrementLikeCount(String diaryId);

    /**
     * 判断用户的点赞状态60s内只能进行15次点赞
     *
     * @param userId 用户id
     * @return
     */
    Boolean judgeLikeStatus(String userId);

    /**
     * 判断该点赞状态是否存在，存在返回其点赞状态
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @param diaryId  日志id
     * @return
     */
    Integer getUserLikeStatus(String userId, String circleId, Long diaryId);
}
