package com.ccl.wx.dto;

import com.ccl.wx.vo.CircleHomeThemeVO;
import com.ccl.wx.vo.DiaryLikeVO;
import lombok.Data;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2019/12/12 20:03
 */
@Data
public class UserDiaryDTO {
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
     * 日志状态（0 全部人员可见 1仅圈子内成员可见 2仅管理员可见）
     */
    private Integer diaryStatus;

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
     * 日志视频地址
     */
    private String diaryVideo;

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
     * 用户连续打卡天数
     */
    private Integer userSignin;

    /**
     * 格式化日期时间
     */
    private String formatCreateTime;

    /**
     * 日志点赞状态
     */
    private Boolean likeStatus;

    /**
     * 全部评论
     */
    private List<CommentDTO> comments;

    /**
     * 全部点评
     */
    private List<CommentDTO> masterComments;

    /**
     * 点赞用户的信息 userid::userNickname
     */
    private List<DiaryLikeVO> likeUserInfos;

    /**
     * 点赞用户全部昵称
     */
    private String likeUserInfosStr;

    /**
     * 是否还有更多数据
     */
    private Boolean hasMoreData;

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

    /**
     * 圈子主题
     */
    private CircleHomeThemeVO themeInfo;
}
