package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/4/16 22:01
 */

@ApiModel(value = "com-ccl-wx-entity-CircleIntro")
@Data
public class CircleIntro implements Serializable {
    /**
     * 圈子id
     */
    @NotNull(message = "圈子id不能为空")
    @ApiModelProperty(value = "圈子id")
    private Integer circleId;

    /**
     * 圈主简介
     */
    @ApiModelProperty(value = "圈主简介")
    private String userIntro;

    /**
     * 圈主简介语音地址
     */
    @ApiModelProperty(value = "圈主简介语音地址")
    private String userVoice;

    /**
     * 圈子简介
     */
    @ApiModelProperty(value = "圈子简介")
    private String circleIntro;

    /**
     * 圈子简介语音地址
     */
    @ApiModelProperty(value = "圈子简介语音地址")
    private String circleVoice;

    /**
     * 圈主简介视频地址
     */
    @ApiModelProperty(value = "圈主简介视频地址")
    private String userVideo;

    /**
     * 圈子简介视频地址
     */
    @ApiModelProperty(value = "圈子简介视频地址")
    private String circleVideo;

    /**
     * 日志图片地址
     */
    @ApiModelProperty(value = "日志图片地址")
    private String circleImage;

    /**
     * 用户图片地址
     */
    @ApiModelProperty(value = "用户图片地址")
    private String userImage;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

    private static final long serialVersionUID = 1L;
}