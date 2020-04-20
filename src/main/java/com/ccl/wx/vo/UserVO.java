package com.ccl.wx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 褚超亮
 * @date 2020/4/20 11:46
 */
@Data
public class UserVO implements Serializable {
    /**
     * 用户的唯一表示
     */
    @ApiModelProperty(value = "用户的唯一表示")
    private String id;

    /**
     * 用户的昵称
     */
    @ApiModelProperty(value = "用户的昵称")
    private String nickname;

    /**
     * 头像地址
     */
    @ApiModelProperty(value = "头像地址")
    private String avatarurl;

    /**
     * 性别（1男2女）
     */
    @ApiModelProperty(value = "性别（1男2女）")
    private String gender;
}
