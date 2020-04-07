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
 * @date 2020/3/25 19:51
 */

@ApiModel(value = "com-ccl-wx-entity-Comment")
@Data
public class Comment implements Serializable {
    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long id;

    /**
     * 日志id
     */
    @ApiModelProperty(value = "日志id")
    @NotNull(message = "日志id不能为空")
    private Long diaryId;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "评论内容不能为空")
    private String commentContent;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date commentCreatetime;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    private Date commentDeltime;

    /**
     * 评论状态(0正常 1已删除)
     */
    @ApiModelProperty(value = "评论状态(0正常 1已删除)")
    private Integer commentStatus;

    /**
     * 评论点赞数
     */
    @ApiModelProperty(value = "评论点赞数")
    private Long commentLike;

    /**
     * 评论类型(0普通评论 1管理员评论)
     */
    @ApiModelProperty(value = "评论类型(0普通评论 1管理员评论)")
    private Integer commentType;

    /**
     * 圈子id
     */
    @ApiModelProperty(value = "圈子id")
    private Long commentCircle;

    /**
     * 圈子id
     */
    @ApiModelProperty(value = "圈子id")
    @NotNull(message = "圈子id不能为空")
    private Long circleId;

    /**
     * 评论照片
     */
    @ApiModelProperty(value = "评论照片")
    private String commentImage;

    private static final long serialVersionUID = 1L;
}