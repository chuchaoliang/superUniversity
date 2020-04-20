package com.ccl.wx.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/4/17 10:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CircleIntroDTO implements Serializable {
    /**
     * 圈子id
     */
    @NotNull(message = "圈子id不能为空")
    @ApiModelProperty(value = "圈子id")
    private Integer circleId;

    @ApiModelProperty(value = "圈主简介")
    private String userIntro;

    @ApiModelProperty(value = "圈主简介语音地址")
    private String userVoice;

    @ApiModelProperty(value = "圈子简介")
    private String circleIntro;

    @ApiModelProperty(value = "圈子简介语音地址")
    private String circleVoice;

    @ApiModelProperty(value = "圈主简介视频地址")
    private String userVideo;

    @ApiModelProperty(value = "圈子简介视频地址")
    private String circleVideo;

    @ApiModelProperty(value = "圈主简介图片列表")
    private List<String> userImages;

    @ApiModelProperty(value = "圈子简介图片列表")
    private List<String> circleImages;

    private static final long serialVersionUID = 1L;
}
