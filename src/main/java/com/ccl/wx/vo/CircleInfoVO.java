package com.ccl.wx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 褚超亮
 * @date 2020/4/10 15:29
 */
@Data
public class CircleInfoVO implements Serializable {
    @ApiModelProperty(value = "圈子id")
    private Long circleId;

    @ApiModelProperty(value = "圈子名称")
    private String circleName;

    @ApiModelProperty(value = "圈子标签")
    private String circleLabel;

    @ApiModelProperty(value = "圈子成员总数")
    private Integer circleMember;

    @ApiModelProperty(value = "日志总数")
    private Integer diarySum;

    @ApiModelProperty(value = "圈子主页图片地址")
    private String circleHimage;

    /**
     * ----------------------------------------------------------
     */
    @ApiModelProperty(value = "是否为私密圈子")
    private Boolean privacy;

    @ApiModelProperty(value = "是否加入此圈子")
    private Boolean join;

    @ApiModelProperty(value = "是否为圈子管理人员")
    private Boolean manage;

    private static final long serialVersionUID = 1L;
}
