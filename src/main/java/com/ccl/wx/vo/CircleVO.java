package com.ccl.wx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 褚超亮
 * @date 2020/4/18 23:11
 */
@Data
public class CircleVO implements Serializable {
    @ApiModelProperty(value = "圈子id")
    private Long circleId;

    @ApiModelProperty(value = "圈子名称")
    private String circleName;

    @ApiModelProperty(value = "圈子主页图片地址")
    private String circleHimage;
}
