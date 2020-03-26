package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 褚超亮
 * @date 2020/3/25 19:33
 */

@ApiModel(value = "com-ccl-wx-entity-UserLike")
@Data
public class UserLike implements Serializable {
    /**
     * 点赞表主键
     */
    @ApiModelProperty(value = "点赞表主键")
    private Long id;

    /**
     * 对应的日志或评论的id
     */
    @ApiModelProperty(value = "对应的日志或评论的id")
    private Long typeId;

    /**
     * 点赞类型(1日志点赞 2评论点赞 3回复点赞)
     */
    @ApiModelProperty(value = "点赞类型(1日志点赞 2评论点赞 3回复点赞)")
    private Integer type;

    /**
     * 点赞的用户id
     */
    @ApiModelProperty(value = "点赞的用户id")
    private String likeUserid;

    /**
     * 点赞状态（0无效 1有效）
     */
    @ApiModelProperty(value = "点赞状态（0无效 1有效）")
    private Integer likeStatus;

    /**
     * 点赞创建时间
     */
    @ApiModelProperty(value = "点赞创建时间")
    private Date likeCreatetime;

    /**
     * 点赞更新时间
     */
    @ApiModelProperty(value = "点赞更新时间")
    private Date likeUpdatetime;

    /**
     * 日志所在的圈子
     */
    @ApiModelProperty(value = "日志所在的圈子")
    private Long circleId;

    private static final long serialVersionUID = 1L;
}