package com.ccl.wx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/4/11 14:30
 */
@Data
public class CircleNormalUserInfoVO implements Serializable {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "加入圈子时间")
    private Date joinTime;

    @ApiModelProperty(value = "用户连续打卡天数")
    private Integer userSignin;

    @ApiModelProperty(value = "用户所在圈子的活跃度")
    private Long userVitality;

    @ApiModelProperty(value = "用户最后打卡时间")
    private Date userSignTime;

    @ApiModelProperty(value = "用户打卡天数")
    private Long userSigninDay;

    // -----------------------------------------------

    @ApiModelProperty(value = "用户的昵称")
    private String nickname;

    @ApiModelProperty(value = "头像地址")
    private String avatarurl;

    @ApiModelProperty(value = "性别（1男2女）")
    private String gender;

    private static final long serialVersionUID = 1L;
}
