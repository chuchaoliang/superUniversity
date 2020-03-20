package com.ccl.wx.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author CCL
 * @date 2019/10/5 19:02
 * 配置文件中配置数据
 */
@Data
@Component
public class LoginData {

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.secret}")
    private String secret;
}
