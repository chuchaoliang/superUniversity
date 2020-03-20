package com.ccl.wx.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author 褚超亮
 * @date 2019/10/24 13:38
 */
@Data
public class PlanDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 内容
     */
    private String content;

    /**
     * 简略内容
     */
    private String briefContent;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 计划删除的时间集合
     */
    private String delTime;

    /**
     * 标志，0是存在，1作废，2删除，3无定向
     */
    private Integer sign;

    /**
     * 跳转链接
     */
    private String url;

    /**
     * 用户的连表值
     */
    private String userid;

    /**
     * 名言
     */
    private String quotes;

    /**
     * 作者
     */
    private String author;

    /**
     * 计划开始时间
     */
    private String titleTime;

    /**
     * 计划结束时间
     */
    private String titleTimeEnd;

    /**
     * 计划完成的时间集合
     */
    private String finish;

    /**
     * 计划是否完成
     */
    private Boolean finishPlan;
}
