package com.ccl.wx.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/4/7 10:28
 */
@Data
public class UserDiaryVO implements Serializable {
    /**
     * 日志id
     */
    @NotNull(message = "日记id不能为空")
    private Long id;

    /**
     * 日志内容
     */
    private String diaryContent;

    /**
     * 此日志对应的主题id
     */
    @NotNull(message = "日志的主题不能为空哦")
    private Integer themeId;

    /**
     * 日志状态（0 全部人员可见 1仅圈子内成员可见 2仅管理员可见）
     */
    @NotNull(message = "日志的状态不能为空")
    private Integer diaryStatus;

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
     * 日记的图片列表
     */
    private List<String> images;
}
