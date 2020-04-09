package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/3/25 20:05
 */

@ApiModel(value = "com-ccl-wx-entity-Reply")
@Data
public class Reply implements Serializable {
    /**
     * 回复id
     */
    @ApiModelProperty(value = "回复id")
    private Long id;

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    @NotNull(message = "评论id不能为空")
    private Long commentId;

    /**
     * 回复类型（暂无使用）
     */
    @ApiModelProperty(value = "回复类型（暂无使用）")
    private Integer replyType;

    /**
     * 回复内容
     */
    @ApiModelProperty(value = "回复内容")
    private String replyContent;

    /**
     * 回复用户id
     */
    @ApiModelProperty(value = "回复用户id")
    private String replyUserid;

    /**
     * 目标用户id
     */
    @ApiModelProperty(value = "目标用户id")
    @NotBlank(message = "目标用户id不能为空")
    private String targetUserid;

    /**
     * 回复的创建时间
     */
    @ApiModelProperty(value = "回复的创建时间")
    private Date replyCreatetime;

    /**
     * 回复的删除时间
     */
    @ApiModelProperty(value = "回复的删除时间")
    private Date replyDeltime;

    /**
     * 回复的状态(0正常 1删除..)
     */
    @ApiModelProperty(value = "回复的状态(0正常 1删除..)")
    private Integer replyStatus;

    /**
     * 回复点赞数
     */
    @ApiModelProperty(value = "回复点赞数")
    private Long replyLike;

    /**
     * 圈子id
     */
    @ApiModelProperty(value = "圈子id")
    @NotNull(message = "圈子id不能为空")
    private Long circleId;

    /**
     * 回复照片
     */
    @ApiModelProperty(value = "回复照片")
    private String replyImage;

    /**
     * 日志id
     */
    @ApiModelProperty(value = "日志id")
    @NotNull(message = "日志id不能为空")
    private Long diaryId;

    private static final long serialVersionUID = 1L;
}