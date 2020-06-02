package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 褚超亮
 * @date 2020/6/2 22:24
 */

@ApiModel(value = "com-ccl-wx-entity-UserNotify")
@Data
public class UserNotify implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 发送者id
     */
    @ApiModelProperty(value = "发送者id")
    private String senderId;

    /**
     * 接收者id
     */
    @ApiModelProperty(value = "接收者id")
    private String targetId;

    /**
     * 用户类型（0普通用户1系统）
     */
    @ApiModelProperty(value = "用户类型（0普通用户1系统）")
    private Byte userType;

    /**
     * 动作类型
     * 0.系统
     * 1.点赞
     * 2.评论
     * 3.点评
     * 4.回复
     * 5.申请
     * 6.拒绝
     * 7.同意
     * 8.淘汰
     * 9.加入
     * 10.退出
     * 11.关注
     * 12.取消关注
     * 13.私信
     * 14.公告（通知）
     */
    @ApiModelProperty(value = "动作类型,0.系统,1.点赞,2.评论,3.点评,4.回复,5.申请,6.拒绝,7.同意,8.淘汰,9.加入,10.退出,11.关注,12.取消关注,13.私信,14.公告（通知）")
    private Byte action;

    /**
     * 资源类型
     * 0日志
     * 1用户
     * 2通知
     * 3私信
     * 4圈子
     */
    @ApiModelProperty(value = "资源类型,0日志,1用户,2通知,3私信,4圈子")
    private Byte resourceType;

    /**
     * 资源id
     */
    @ApiModelProperty(value = "资源id")
    private Integer resourceId;

    /**
     * 是否删除(0未删除1删除)
     */
    @ApiModelProperty(value = "是否删除(0未删除1删除)")
    private Byte delete;

    /**
     * 是否阅读(0未读1已读)
     */
    @ApiModelProperty(value = "是否阅读(0未读1已读)")
    private Byte read;

    /**
     * 所在位置
     * 0.通知
     * 1.评论
     * 2.点评
     * 3.点赞4.私信
     */
    @ApiModelProperty(value = "所在位置,0.通知,1.评论,2.点评,3.点赞4.私信")
    private Byte location;

    /**
     * 阅读时间
     */
    @ApiModelProperty(value = "阅读时间")
    private Date readTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}