package com.ccl.wx.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 褚超亮
 * @date 2020/3/13 16:58
 */
@Data
@Component
public class DefaultProperties {

    /**
     * 微信中的默认图片
     */
    @Value("${circle.head.image}")
    private String defaultImage;

    /**
     * 小程序的圈子标题默认图片
     */
    @Value("${circle.theme.image}")
    private String defaultThemeImage;

    /**
     * 圈子主题默认标题
     */
    @Value("${circle.theme.title}")
    private String defaultThemeTitle;

    /**
     * 默认圈子头像
     */
    @Value("${user.head.image}")
    private String defaultUserHeadImage;

    /**
     * 用户头像默认上传路径
     */
    @Value("${user.head.image.path}")
    private String defaultUserHeadImagePath;

    private String defaultNginxPath;

    /**
     * 默认主题id
     */
    @Value("${circle.theme.id}")
    private Integer defaultThemeId;
}
