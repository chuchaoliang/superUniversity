package com.ccl.wx.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2019/11/10 14:43
 */
@ApiModel(description = "用户圈子DTO")
@Data
public class CircleInfoDTO {
    /**
     * 圈子id
     */
    private Long circleId;

    /**
     * 圈子名称
     */
    private String circleName;

    /**
     * 圈子活力值
     */
    private Long circleVitality;

    /**
     * 圈子简介
     */
    private String circleIntro;

    /**
     * 今日签到人数
     */
    private Long circleSignin;

    /**
     * 圈子创建时间
     */
    private Date circleCreatetime;

    /**
     * 圈子更新时间
     */
    private Date circleUpdatetime;

    /**
     * 圈子中的图片地址
     */
    private String circleImage;

    /**
     * 圈主id
     */
    private String circleUserid;

    /**
     * 圈主简介
     */
    private String circleUserintro;

    /**
     * 圈子解散时间
     */
    private Date circleDeltime;

    /**
     * 圈子状态(0 正常 1解散)
     */
    private Integer circleSign;

    /**
     * 圈子所在位置（1 技能 2 考研 3生活 4阅读 5外语 6财汇 7计算机 8更多 ）
     */
    private Integer circleLocation;

    /**
     * 圈子设置(0直接加入 1需要圈主同意)
     */
    private Integer circleSet;

    /**
     * 圈子主页图片地址
     */
    private String circleHimage;

    /**
     * 圈子标签（20字以内）
     */
    private String circleLabel;

    /**
     * 圈子成员总数
     */
    private Integer circleMember;

    /**
     * 用户是否加入此圈子
     */
    private Boolean userJoin;

    /**
     * 用户是否为圈子管理员
     */
    private Boolean userMaster;

    /**
     * 圈子内容
     */
    private String todayContent;

    /**
     * 圈子内容创建时间
     */
    private Date todayCreateTime;

    /**
     * 圈子中图片地址
     */
    private List<String> todayImages;

    /**
     * 每日打卡总人数
     */
    private Integer clockInSum;

    /**
     * 圈子中日志总数
     */
    private Integer diaryNumber;

    /**
     * 圈子是否存在通知
     */
    private Boolean circleNotice;

    /**
     * 圈子通知内容
     */
    private String circleNoticeContent;

    /**
     * 是否需要申请才能加入圈子
     */
    private Boolean applyJoin;
}
