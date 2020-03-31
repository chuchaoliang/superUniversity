package com.ccl.wx.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 褚超亮
 * @date 2020/3/30 14:22
 */
@Data
public class CircleHomeThemeVO implements Serializable {
    /**
     * 今日内容主键
     */
    private Long id;

    /**
     * 主题标题
     */
    private String themeTitle;
}
