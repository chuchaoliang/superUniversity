package com.ccl.wx.service;

/**
 * @author 褚超亮
 * @date 2020/1/27 16:22
 */
public interface CircleRedisService {
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
     * @param userId
     * @param circleId
     * @param diaryId
     * @return
     */
    String unLikeFromRedis(String userId, String circleId, String diaryId);

    /**
     * 删除点赞数据
     *
     * @param userId
     * @param circleId
     * @param diaryId
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
     * @param diaryId
     * @return
     */
    String decrementLikeCount(String diaryId);

    /**
     * 判断用户的点赞状态60s内只能进行15次点赞
     *
     * @param userid
     * @return
     */
    Boolean judgeLikeStatus(String userid);

    /**
     * 判断该点赞状态是否存在，存在返回其点赞状态
     * @param userId 用户id
     * @param circleId
     * @param diaryId
     * @return
     */
    Integer getUserLikeStatus(String userId, String circleId, Long diaryId);
}
