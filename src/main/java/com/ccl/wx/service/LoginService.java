package com.ccl.wx.service;

/**
 * @author 褚超亮
 * @date 2019/10/14 18:16
 */
public interface LoginService {
    /**
     * 将session_key，openid字符串处理后 转为json对象返回
     *
     * @param sourceStr
     * @return
     */
    String encryptionSessionKey(String sourceStr);
}
