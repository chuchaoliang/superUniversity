package com.ccl.wx.service;

import com.ccl.wx.entity.JoinCircle;

import java.util.List;
import java.util.Map;

/**
 * @author 褚超亮
 * @date 2020/3/7 17:19
 */

public interface JoinCircleService {


    int deleteByPrimaryKey(Long circleId, String userId);

    int insert(JoinCircle record);

    int insertSelective(JoinCircle record);

    JoinCircle selectByPrimaryKey(Long circleId, String userId);

    int updateByPrimaryKeySelective(JoinCircle record);

    int updateByPrimaryKey(JoinCircle record);

    /**
     * 拼接圈子id
     *
     * @param circleId
     * @param userId
     * @param themeId
     * @param flag
     * @return
     */
    int concatCircleTheme(Long circleId, String userId, String themeId, Boolean flag);

    /**
     * 更新圈子全部成员打卡状态
     *
     * @param circleId
     * @param signInStatus
     * @return
     */
    int updateCircleSignInStatus(Long circleId, Integer signInStatus);

    /**
     * 根据圈子用户状态获取总人数
     *
     * @param circleId
     * @param userStatus
     * @return
     */
    int countByCircleIdAndUserStatus(Long circleId, Integer userStatus);

    /**
     * 获取积分排行榜
     *
     * @param circleId
     * @param page
     * @return
     */
    String getUserVitalityRankingData(Long circleId, Integer page);

    /**
     * 获取连续打卡排行榜
     *
     * @param circleId
     * @param page
     * @return
     */
    String getUserSignInRankingData(Long circleId, Integer page);

    /**
     * 用户活跃排行榜
     *
     * @param circleId   圈子id
     * @param start      开始页数
     * @param pageNumber 每页数量
     * @return
     */
    List<Map> getUserVitalityRanking(Long circleId, Integer start, Integer pageNumber);

    /**
     * 用户连续打卡排行榜
     *
     * @param circleId
     * @param start
     * @param pageNumber
     * @return
     */
    List<Map> getUserSignInRanking(Long circleId, Integer start, Integer pageNumber);

    /**
     * 获取某人的活跃度排名信息
     *
     * @param circleId
     * @param userId
     * @param start
     * @param pageNumber
     * @return
     */
    List<Map> getUserVitalityRankingInfo(Long circleId, String userId, Integer start, Integer pageNumber);

    /**
     * 获取某人的连续打卡排名信息
     *
     * @param circleId
     * @param userId
     * @param start
     * @param pageNumber
     * @return
     */
    List<Map> getUserSignInRankingInfo(Long circleId, String userId, Integer start, Integer pageNumber);

    /**
     * 获取用户活跃度排名信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    List<Map> getUserVitalityInfo(Long circleId, String userId);

    /**
     * 获取用户连续打卡排名信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    List<Map> getUserSignInInfo(Long circleId, String userId);
}
