package com.ccl.wx.vo;

import com.ccl.wx.entity.UserInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/30 13:14
 */
@Data
public class CircleHomeDiaryVO implements Serializable {

    /**
     * 日志id
     */
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 日志内容
     */
    private String diaryContent;

    /**
     * 日志点赞数
     */
    private Integer diaryLike;

    /**
     * 日志评论数
     */
    private Integer diaryComment;

    /**
     * 日志的位置信息
     */
    private String diaryAddress;

    /**
     * 日志的录音地址
     */
    private String diaryVoice;

    /**
     * 日志所在的圈子id
     */
    private Long circleId;

    /**
     * 创建时间的加工 几天前 几个月前 几个小时前
     */
    private String createTimeRelative;

    /**
     * 日记的图片列表
     */
    private List<String> images;

    /**
     * 用户头像照片
     */
    private String userHeadImage;

    /**
     * 用户性别
     */
    private String userGender;

    /**
     * 用户昵称
     */
    private String userNickName;

    /**
     * 用户打卡数
     */
    private Long userSignNumber;

    /**
     * 日志点赞状态
     */
    private Boolean likeStatus;

    /**
     * 全部评论
     */
    private List<CircleHomeCommentVO> comments;

    /**
     * 全部点评
     */
    private List<CircleHomeCommentVO> masterComments;

    /**
     * 点赞用户的信息 userid::userNickname
     */
    private List<UserInfo> likeUserInfos;

    /**
     * 点赞用户全部昵称
     */
    private String likeUserInfosStr;

    /**
     * 是否省略文本内容
     */
    private Boolean ellipsis;

    /**
     * 固定 是否省略文本内容
     */
    private Boolean judgeEllipsis;

    /**
     * 是否隐藏评论信息
     */
    private Boolean hideComment;

    /**
     * 日志评论和回复的总数
     */
    private Long commentSum;

    /**
     * 是否隐藏点赞信息
     */
    private Boolean likeComment;
}
