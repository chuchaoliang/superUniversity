package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/3/7 10:15
 */

@ApiModel(value = "com-ccl-wx-entity-UserDiary")
@Data
public class UserDiary implements Serializable {
    /**
     * 日志id
     */
    @ApiModelProperty(value = "日志id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 日志所在的圈子id
     */
    @ApiModelProperty(value = "日志所在的圈子id")
    private Long circleId;

    /**
     * 此日志对应的主题id
     */
    @ApiModelProperty(value = "此日志对应的主题id")
    private Integer themeId;

    /**
     * 日志创建时间
     */
    @ApiModelProperty(value = "日志创建时间")
    private Date diaryCreatetime;

    /**
     * 日志更新时间
     */
    @ApiModelProperty(value = "日志更新时间")
    private Date diaryUpdatetime;

    /**
     * 日志文本内容
     */
    @ApiModelProperty(value = "日志文本内容")
    private String diaryContent;

    /**
     * 日志点赞数
     */
    @ApiModelProperty(value = "日志点赞数")
    private Integer diaryLike;

    /**
     * 日志浏览量
     */
    @ApiModelProperty(value = "日志浏览量")
    private Integer diaryBrowse;

    /**
     * 日志状态（0 全部人员可见(正常状态) 1仅圈子内成员可见 2日志删除状态）
     */
    @ApiModelProperty(value = "日志状态（0 全部人员可见(正常状态) 1仅圈子内成员可见 2日志删除状态）")
    private Integer diaryStatus;

    /**
     * 日志评论、回复数
     */
    @ApiModelProperty(value = "日志评论、回复数")
    private Integer diaryComment;

    /**
     * 日志删除时间
     */
    @ApiModelProperty(value = "日志删除时间")
    private Date diaryDeltime;

    /**
     * 日志图片地址
     */
    @ApiModelProperty(value = "日志图片地址")
    private String diaryImage;

    /**
     * 日志的位置信息
     */
    @ApiModelProperty(value = "日志的位置信息")
    private String diaryAddress;

    /**
     * 日志的录音地址
     */
    @ApiModelProperty(value = "日志的录音地址")
    private String diaryVoice;

    /**
     * 日志的视频地址
     */
    @ApiModelProperty(value = "日志的视频地址")
    private String diaryVideo;

    private static final long serialVersionUID = 1L;
}