package com.ccl.wx.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 圈子每日内容DTO类
 * @author 褚超亮
 * @date 2020/1/12 10:31
 */
@Data
public class CircleTodayContentDTO {
    /**
     * 今日内容主键
     */
    private Long id;

    /**
     * 今日内容所在圈子id
     */
    private Long circleId;

    /**
     * 今日内容
     */
    private String todayContent;

    /**
     * 今日内容图片地址
     */
    private String todayImage;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 删除时间
     */
    private Date deleteTime;

    /**
     * 今日内容更新时间
     */
    private Date updateTime;

    /**
     * 今日内容状态（0正常1已经更新2已经删除）
     */
    private Integer contentStatus;

    /**
     * 阅读数
     */
    private Long readNumber;

    /**
     * 圈子图片列表
     */
    private List<String> todayImages;
}
