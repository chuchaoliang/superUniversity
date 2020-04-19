package com.ccl.wx.dto;

import lombok.Data;

/**
 * @author 褚超亮
 * @date 2020/1/21 18:59
 */
@Data
public class ReplyDTO {
    /**
     * 回复id
     */
    private Long id;

    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 回复用户id
     */
    private String replyUserid;

    /**
     * 目标人用户id
     */
    private String targetUserid;

    /**
     * 回复的创建时间
     */
    private String createTime;

    /**
     * 回复点赞数
     */
    private Long replyLike;

    /**
     * 回复照片
     */
    private String replyImage;

    /**
     * 回复人昵称
     */
    private String rNickName;

    /**
     * 目标人昵称
     */
    private String tNickName;

    /**
     * 回复人性别
     */
    private String rGender;

    /**
     * 目标人性别
     */
    private String tGender;

    /**
     * 回复人头像
     */
    private String rHeadImage;

    /**
     * 回复人头像
     */
    private String tHeadImage;
}
