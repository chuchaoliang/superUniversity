package com.ccl.wx.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/1/18 12:46
 */
@Data
public class CommentDTO {
    /**
     * 评论id
     */
    private Long id;

    /**
     * 评论内容（200个字以内）
     */
    private String commentContent;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 评论点赞数
     */
    private Long commentLike;

    /**
     * 评论照片
     */
    private String commentImage;

    /**
     * 评论人昵称
     */
    private String nickName;

    /**
     * 评论人头像
     */
    private String headImage;

    /**
     * 评论人性别
     */
    private String gender;

    /**
     * 回复总数
     */
    private Long replyNumber;

    /**
     * 回复列表
     */
    private List<ReplyDTO> replyList;
}
