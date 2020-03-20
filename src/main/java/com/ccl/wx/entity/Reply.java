package com.ccl.wx.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/1/21 19:25
 */

@Data
public class Reply {
    /**
     * 回复id
     */
    private Long id;

    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 回复类型（暂无使用）
     */
    private Integer replyType;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 回复用户id
     */
    private String replyUserid;

    /**
     * 目标用户id
     */
    private String targetUserid;

    /**
     * 回复的创建时间
     */
    private Date replyCreatetime;

    /**
     * 回复的删除时间
     */
    private Date replyDeltime;

    /**
     * 回复的状态(0正常 1删除..)
     */
    private Integer replyStatus;

    /**
     * 回复点赞数
     */
    private Long replyLike;

    /**
     * 圈子id
     */
    private Long circleId;

    /**
     * 回复照片
     */
    private String replyImage;

    /**
     * 日志id
     */
    private Long diaryId;
}