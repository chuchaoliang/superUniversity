package com.ccl.wx.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/29 17:22
 */
@Data
public class CircleIndexVO implements Serializable {
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
     * 圈子中日志总数
     */
    private Integer diaryNumber;

    /**
     * 圈子是否存在通知
     */
    private Boolean circleNotice;

    /**
     * 是否需要申请才能加入圈子
     */
    private Boolean applyJoin;

    /**
     * 圈子通知内容
     */
    private String circleNoticeContent;

    /**
     * 圈子主题列表
     */
    private List<CircleThemeVO> circleThemeList;
}
