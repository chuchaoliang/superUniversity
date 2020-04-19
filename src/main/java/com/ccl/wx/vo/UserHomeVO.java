package com.ccl.wx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户个人主页信息
 *
 * @author 褚超亮
 * @date 2020/4/19 18:32
 */
@Data
public class UserHomeVO implements Serializable {

    @ApiModelProperty(value = "用户的唯一表示")
    private String id;

    @ApiModelProperty(value = "用户的昵称")
    private String nickname;

    @ApiModelProperty(value = "头像地址")
    private String avatarurl;

    @ApiModelProperty(value = "性别（1男2女）")
    private String gender;

    @ApiModelProperty(value = "用户标签")
    private String userLabel;

    @ApiModelProperty(value = "用户的能量球")
    private Integer vitality;

    @ApiModelProperty(value = "关注数")
    private Integer attention;

    @ApiModelProperty(value = "粉丝数")
    private Integer fans;

    @ApiModelProperty(value = "点赞数")
    private Integer like;
}
