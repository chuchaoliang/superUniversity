package com.ccl.wx.properties;

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
}
