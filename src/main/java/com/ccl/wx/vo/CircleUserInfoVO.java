package com.ccl.wx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/4/11 20:20
 */
@Data
public class CircleUserInfoVO implements Serializable {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "加入圈子时间")
    private Date joinTime;

    @ApiModelProperty(value = "用户退出时间")
    private Date exitTime;

    @ApiModelProperty(value = "申请理由")
    private String applyReason;

    @ApiModelProperty(value = "拒绝加入圈子的理由")
    private String refuseReason;
    // -----------------------------------------------

    @ApiModelProperty(value = "用户的昵称")
    private String nickname;

    @ApiModelProperty(value = "头像地址")
    private String avatarurl;

    @ApiModelProperty(value = "性别（1男2女）")
    private String gender;

    private static final long serialVersionUID = 1L;
}
