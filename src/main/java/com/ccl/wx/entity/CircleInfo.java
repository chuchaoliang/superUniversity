package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/3/6 15:36
 */

@ApiModel(value = "com-ccl-wx-entity-CircleInfo")
@Data
public class CircleInfo implements Serializable {
    /**
     * 圈子id
     */
    @ApiModelProperty(value = "圈子id")
    private Long circleId;

    /**
     * 圈子名称
     */
    @ApiModelProperty(value = "圈子名称")
    @NotBlank(message = "圈子名称不能为空！nickName")
    private String circleName;

    /**
     * 圈子活力值
     */
    @ApiModelProperty(value = "圈子活力值")
    private Long circleVitality;

    /**
     * 圈子介绍
     */
    @ApiModelProperty(value = "圈子介绍")
    private String circleIntro;

    /**
     * 圈子签到总人数
     */
    @ApiModelProperty(value = "圈子签到总人数")
    private Long circleSignin;

    /**
     * 圈子创建时间
     */
    @ApiModelProperty(value = "圈子创建时间")
    private Date circleCreatetime;

    /**
     * 圈子更新时间
     */
    @ApiModelProperty(value = "圈子更新时间")
    private Date circleUpdatetime;

    /**
     * 圈子最新主题id
     */
    @ApiModelProperty(value = "圈子最新主题id")
    private String circleTask;

    /**
     * 圈子中的图片地址
     */
    @ApiModelProperty(value = "圈子中的图片地址")
    private String circleImage;

    /**
     * 圈主id
     */
    @ApiModelProperty(value = "圈主id")
    @NotBlank(message = "圈主id不能为空circleUserid")
    private String circleUserid;

    /**
     * 圈主简介
     */
    @ApiModelProperty(value = "圈主简介")
    private String circleUserintro;

    /**
     * 圈子解散时间
     */
    @ApiModelProperty(value = "圈子解散时间")
    private Date circleDeltime;

    /**
     * 圈子状态(0 正常 1解散 2推荐)
     */
    @ApiModelProperty(value = "圈子状态(0 正常 1解散 2推荐)")
    private Integer circleSign;

    /**
     * 圈子所在位置（1技能 2考研 3生活 4阅读 5健身 6学校 7考试 8科研 9竞赛 10更多 ）
     */
    @ApiModelProperty(value = "圈子所在位置（1技能 2考研 3生活 4阅读 5健身 6学校 7考试 8科研 9竞赛 10更多 ）")
    @NotBlank(message = "圈子所在位置不能为空->circleLocation")
    private Integer circleLocation;

    /**
     * 圈子设置(0直接加入 1需要圈主同意 2私密圈子)
     */
    @ApiModelProperty(value = "圈子设置(0直接加入 1需要圈主同意 2私密圈子)")
    private Integer circleSet;

    /**
     * 圈子主页图片地址
     */
    @ApiModelProperty(value = "圈子主页图片地址")
    private String circleHimage;

    /**
     * 圈子标签
     */
    @ApiModelProperty(value = "圈子标签")
    @NotBlank(message = "圈子标签不能为空！-> circleLabel")
    private String circleLabel;

    /**
     * 圈子密码
     */
    @ApiModelProperty(value = "圈子密码")
    private String circlePassword;

    /**
     * 圈子成员总数
     */
    @ApiModelProperty(value = "圈子成员总数")
    private Integer circleMember;

    /**
     * 主题总数
     */
    @ApiModelProperty(value = "主题总数")
    private Integer themeSum;

    private static final long serialVersionUID = 1L;
}