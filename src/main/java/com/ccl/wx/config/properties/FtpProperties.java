package com.ccl.wx.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 褚超亮
 * @date 2019/11/29 22:30
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.ftp")
public class FtpProperties {
    private String host;
    private int port;
    private String user;
    private String password;
    private String basePath;
    private String httpPath;
}
