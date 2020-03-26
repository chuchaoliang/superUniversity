package com.ccl.wx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/3/26 13:42
 */
@Data
public class CircleThemeVO implements Serializable {

    /**
     * 主题id
     */
    private Long id;

    /**
     * 圈子id
     */
    private Long circleId;

    /**
     * 主题标题
     */
    private String themeTitle;

    /**
     * 主题头像
     */
    @ApiModelProperty(value = "主题头像")
    private String headImage;

    /**
     * 主题创建时间
     */
    private Date createTime;

    /**
     * 此主题下的日记总数
     */
    private Integer diaryNumber;

    /**
     * 主题内容
     */
    private String todayContent;

    /**
     * 此主题是否完成打卡
     */
    private Boolean signInSuccess;

    /**
     * 是否为圈主
     */
    private Boolean circleMaster;

    /**
     * 是否为默认主题
     */
    private Boolean defaultTheme;
}
