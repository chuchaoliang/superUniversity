package com.ccl.wx.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 褚超亮
 * @date 2020/3/30 13:21
 */
@Data
public class CircleHomeReplyVO implements Serializable {
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
     * 目标用户id
     */
    private String targetUserid;

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
    private String nickName;

    /**
     * 目标人昵称
     */
    private String targetNickName;
}
