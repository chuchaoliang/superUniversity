package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 褚超亮
 * @date 2020/3/12 20:47
 */

@ApiModel(value = "com-ccl-wx-entity-TodayContent")
@Data
public class TodayContent implements Serializable {
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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

    /**
     * 今日内容更新时间
     */
    @ApiModelProperty(value = "今日内容更新时间")
    private Date updateTime;

    /**
     * 今日内容状态（0正常1已经更新2已经删除）
     */
    @ApiModelProperty(value = "今日内容状态（0正常1已经更新2已经删除）")
    private Integer contentStatus;

    /**
     * 阅读数
     */
    @ApiModelProperty(value = "阅读数")
    private Long readNumber;

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
     * 阅读的用户
     */
    @ApiModelProperty(value = "阅读的用户")
    private String readUser;

    private static final long serialVersionUID = 1L;
}