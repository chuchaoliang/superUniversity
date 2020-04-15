package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/4/11 20:52
 */

@ApiModel(value = "com-ccl-wx-entity-JoinCircle")
@Data
public class JoinCircle implements Serializable {
    /**
     * 圈子id
     */
    @ApiModelProperty(value = "圈子id")
    private Long circleId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 加入圈子时间
     */
    @ApiModelProperty(value = "加入圈子时间")
    private Date joinTime;

    /**
     * 用户状态（0正常 1待加入圈子需要圈主同意 2淘汰 3加入圈子被拒绝 4退出）
     */
    @ApiModelProperty(value = "用户状态（0正常 1待加入圈子需要圈主同意 2淘汰 3加入圈子被拒绝 4退出）")
    private Integer userStatus;

    /**
     * 用户连续打卡天数
     */
    @ApiModelProperty(value = "用户连续打卡天数")
    private Integer userSignin;

    /**
     * 用户权限（0正常用户 1管理员用户 2圈主）
     */
    @ApiModelProperty(value = "用户权限（0正常用户 1管理员用户 2圈主）")
    private Integer userPermission;

    /**
     * 用户所在圈子的活跃度
     */
    @ApiModelProperty(value = "用户所在圈子的活跃度")
    private Long userVitality;

    /**
     * 用户所在圈子的昵称
     */
    @ApiModelProperty(value = "用户所在圈子的昵称")
    private String userNickName;

    /**
     * 用户最后打卡时间
     */
    @ApiModelProperty(value = "用户最后打卡时间")
    private Date userSignTime;

    /**
     * 0未签到 1签到成功 2打卡全部主题
     */
    @ApiModelProperty(value = "0未签到 1签到成功 2打卡全部主题")
    private Integer userSignStatus;

    /**
     * 用户打卡天数
     */
    @ApiModelProperty(value = "用户打卡天数")
    private Long userSigninDay;

    /**
     * 用户退出时间
     */
    @ApiModelProperty(value = "用户退出时间")
    private Date exitTime;

    /**
     * 拒绝加入圈子的理由
     */
    @ApiModelProperty(value = "拒绝加入圈子的理由")
    private String refuseReason;

    /**
     * 淘汰圈子成员的理由
     */
    @ApiModelProperty(value = "淘汰圈子成员的理由")
    private String outReason;

    /**
     * 成员签到日期
     */
    @ApiModelProperty(value = "成员签到日期")
    private String clockinCalendar;

    /**
     * 打卡的主题id
     */
    @ApiModelProperty(value = "打卡的主题id")
    private String themeId;

    /**
     * 申请理由
     */
    @ApiModelProperty(value = "申请理由")
    private String applyReason;

    private static final long serialVersionUID = 1L;
}