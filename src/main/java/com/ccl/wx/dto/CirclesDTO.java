package com.ccl.wx.dto;

import lombok.Data;

import java.util.Date;

/**
 * 圈子信息加强类
 * @author 褚超亮
 * @date 2019/11/14 20:08
 */
@Data
public class CirclesDTO {
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
     * 圈子每日任务
     */
    private String circleTask;

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
     * 添加总人数
     */
    private Integer joinPerson;

    /**
     * 日记总数
     */
    private Long sumDiary;

    /**
     * 是否为圈主 TODO 待添加 目前前端判断
     */
    //private Boolean circleMaster;
}
