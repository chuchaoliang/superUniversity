package com.ccl.wx.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/1/28 21:35
 */
@Data
public class UserLikeDTO {
    /**
     * 点赞表主键
     */
    private Long id;

    /**
     * 对应的日志或评论的id
     */
    private Long typeId;

    /**
     * 点赞类型(1日志点赞 2评论点赞 3回复点赞)
     */
    private Integer type;

    /**
     * 点赞的用户id
     */
    private String likeUserid;

    /**
     * 点赞状态（0无效 1有效）
     */
    private Integer likeStatus;

    /**
     * 点赞创建时间
     */
    private Date likeCreatetime;

    /**
     * 点赞更新时间
     */
    private Date likeUpdatetime;

    /**
     * 日志所在的圈子
     */
    private Long circleId;

    /**
     * 所有点赞用户昵称
     */
    private List<String> nickName;
}
