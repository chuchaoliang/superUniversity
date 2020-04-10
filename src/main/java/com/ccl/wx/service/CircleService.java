package com.ccl.wx.service;

import com.ccl.wx.entity.JoinCircle;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2019/10/30 20:47
 */
public interface CircleService {

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
}
