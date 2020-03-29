package com.ccl.wx.pojo;

import lombok.Data;

/**
 * @author CCL
 * @date 2019/10/6 9:20
 */
@Data
public class UserSession {

    /**
     * 用户的openid
     */
    private String openid;

    /**
     * 处理后的sessionKey
     */
    private String encryptSessionKey;
}
