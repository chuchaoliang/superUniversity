package com.ccl.wx.dto;

import lombok.Data;

import java.util.Date;
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
     * 日志id
     */
    private Long diaryId;

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
    private Date commentCreatetime;

    /**
     * 删除时间
     */
    private Date commentDeltime;

    /**
     * 评论状态(0正常 1已删除)
     */
    private Integer commentStatus;

    /**
     * 评论点赞数
     */
    private Long commentLike;

    /**
     * 评论类型(0普通评论 1管理员评论)
     */
    private Integer commentType;

    /**
     * 圈子id
     */
    private Long circleId;

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
    private List<ReplyDTO> replyDTOS;
}
