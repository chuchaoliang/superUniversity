package com.ccl.wx.dto;

import lombok.Data;

import java.util.Date;

/**
 * 圈子中成员加强类
 * @author 褚超亮
 * @date 2019/11/17 21:36
 */
@Data
public class CircleMemberDTO {
    /**
     * 圈子id
     */
    private Long circleId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 加入圈子时间
     */
    private Date joinTime;

    /**
     * 用户状态（0正常 1待加入圈子需要圈主同意 2淘汰 3加入圈子被拒绝）
     */
    private Integer userStatus;

    /**
     * 用户连续打卡天数
     */
    private Integer userSignin;

    /**
     * 用户权限（0正常用户 1管理员用户 2圈主）
     */
    private Integer userPermission;

    /**
     * 用户所在圈子的活跃度
     */
    private Long userVitality;

    /**
     * 用户所在圈子的昵称
     */
    private String userNickName;

    /**
     * 用户最后打卡时间
     */
    private Date userSignTime;

    /**
     * 0未签到 1签到成功
     */
    private Integer userSignStatus;

    /**
     * 用户打卡天数
     */
    private Long userSigninDay;

    /**
     * 用户退出时间
     */
    private Date exitTime;

    /**
     * 拒绝加入圈子的理由
     */
    private String refuseReason;

    /**
     * 淘汰圈子成员的理由
     */
    private String outReason;

    /**
     * 成员两个月内的签到日期
     */
    private String clockinCalendar;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String imageUrl;

    /**
     * 用户性别
     */
    private String gender;
}
