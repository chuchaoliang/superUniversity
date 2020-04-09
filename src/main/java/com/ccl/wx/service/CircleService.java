package com.ccl.wx.service;

import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.entity.JoinCircle;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2019/10/30 20:47
 */
public interface CircleService {

    /**
     * 判断用户是否为圈主
     * 是圈主: true
     * 不是圈主: false
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    Boolean judgeUserCircleMaster(String circleId, String userId);

    /**
     * 圈子加强类型
     *
     * @param circles 圈子成员信息
     * @return
     */
    String selectCircleDTO(List<CircleInfo> circles);

    /**
     * 圈子后台成员
     *
     * @param circles 圈子成员信息
     * @return
     */
    String selectCircleAllMemberDTO(List<JoinCircle> circles);

    /**
     * 更新圈子头像
     *
     * @param circleid 圈子id
     * @param filePath 图片路径
     * @return
     */
    Boolean updateCircleHeadImage(String circleid, String filePath);

    /**
     * 获取圈子内全部状态的成员
     *
     * @param circleid 圈子id
     * @return
     */
    List<String> getCircleAllMember(String circleid);

    /**
     * 获取用户加入在某个圈子的信息
     *
     * @param userid   用户id
     * @param circleid 圈子id
     * @return
     */
    String getUserInCircleInfo(String userid, Long circleid);

    /**
     * 判断用户是否可以直接进入该圈子
     *
     * @param userid   用户id
     * @param circleid 圈子id
     * @return
     */
    Boolean judgeUserIntoPrivacyCircle(String userid, Long circleid);

    /**
     * 根据圈子id判断是否为私密圈子
     *
     * @param circleId 圈子id
     * @return
     */
    Boolean judgeCirclePrivacyStatus(Long circleId);
}
