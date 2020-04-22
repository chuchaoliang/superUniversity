package com.ccl.wx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 褚超亮
 * @date 2020/4/22 17:53
 */
@Data
public class CircleNoticeVO implements Serializable {

    @NotNull(message = "圈子id不能为空")
    @ApiModelProperty("圈子id")
    private Integer circleId;

    @ApiModelProperty("圈子公告通知内容")
    private String notice;
}
