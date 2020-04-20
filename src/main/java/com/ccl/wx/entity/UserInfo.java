package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/4/19 18:21
 */

@ApiModel(value = "com-ccl-wx-entity-UserInfo")
@Data
public class UserInfo implements Serializable {
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

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;

    /**
     * 国
     */
    @ApiModelProperty(value = "国")
    private String country;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createtime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updatetime;

    /**
     * 用户自增id
     */
    @ApiModelProperty(value = "用户自增id")
    private Long userId;

    /**
     * 用户uid
     */
    @ApiModelProperty(value = "用户uid")
    private String uid;

    /**
     * 用户标签
     */
    @ApiModelProperty(value = "用户标签")
    private String userLabel;

    private static final long serialVersionUID = 1L;
}