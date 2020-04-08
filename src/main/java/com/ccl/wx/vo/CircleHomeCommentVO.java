package com.ccl.wx.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/30 13:17
 */
@Data
public class CircleHomeCommentVO implements Serializable {
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
     * 评论照片
     */
    private String commentImage;

    /**
     * 是否是点评状态
     */
    private Boolean remarkStatus;

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
     * 评论表列表
     */
    private List<CircleHomeReplyVO> replies;
}