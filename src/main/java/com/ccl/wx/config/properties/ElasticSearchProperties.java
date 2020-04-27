package com.ccl.wx.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 褚超亮
 * @date 2020/4/24 13:33
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.elasticsearch")
public class ElasticSearchProperties {
    private String hostname;
    private Integer port;
    private String scheme;
}
