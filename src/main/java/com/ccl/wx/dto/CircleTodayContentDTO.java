package com.ccl.wx.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 圈子每日内容DTO类
 *
 * @author 褚超亮
 * @date 2020/1/12 10:31
 */
@Data
public class CircleTodayContentDTO {

    /**
     * 圈子主题主键
     */
    private Long id;

    /**
     * 圈子主题所在圈子id
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
     * 今日内容状态（0正常1删除）
     */
    private Integer contentStatus;

    /**
     * 阅读数
     */
    private Long readNumber;

    /**
     * 主题标题
     */
    private String themeTitle;

    /**
     * 主题头像
     */
    @ApiModelProperty(value = "主题头像")
    private String headImage;

    /**
     * 视频路径
     */
    @ApiModelProperty(value = "视频路径")
    private String themeVideo;

    /**
     * 声音路径
     */
    @ApiModelProperty(value = "声音路径")
    private String themeVoice;

    /**
     * 圈子图片列表
     */
    private List<String> todayImages;

    /**
     * 主题日志的总数量
     */
    private Integer diaryNumber;
}
