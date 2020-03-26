package com.ccl.wx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/3/13 15:08
 */
@ApiModel(value = "com-ccl-wx-dto-CircleThemeDTO")
@Data
public class CircleThemeDTO {
    /**
     * 今日内容主键
     */
    @ApiModelProperty(value = "今日内容主键")
    private Long id;

    /**
     * 今日内容所在圈子id
     */
    @ApiModelProperty(value = "今日内容所在圈子id")
    private Long circleId;

    /**
     * 今日内容
     */
    @ApiModelProperty(value = "今日内容")
    private String todayContent;

    /**
     * 今日内容图片地址
     */
    @ApiModelProperty(value = "今日内容图片地址")
    private String todayImage;

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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 主题头像
     */
    @ApiModelProperty(value = "主题头像")
    private String headImage;

    /**
     * 主题标题
     */
    @ApiModelProperty(value = "主题标题")
    private String themeTitle;

    /**
     * 用户是否打卡标志
     */
    @ApiModelProperty(value = "用户是否打卡")
    private Boolean judgeClockIn;

    /**
     * 此主题的打卡总人数
     */
    @ApiModelProperty(value = "打卡日记总数")
    private Integer diaryNumber;
}
