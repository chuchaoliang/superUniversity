package com.ccl.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccl.wx.pojo.UserSession;
import com.ccl.wx.service.LoginService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 褚超亮
 * @date 2019/10/14 18:16
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public String encryptionSessionKey(String sourceStr) {
        JSONObject strJson = JSONObject.parseObject(sourceStr);
        String sessionKey = strJson.getString("session_key");
        String openid = strJson.getString("openid");
        // TODO 获取session_key的hash值 可以重构用更好的的方式处理。。。
        String finalSessionKey = Integer.toHexString(sessionKey.hashCode());
        UserSession userSession = new UserSession();
        userSession.setFinalSessionKey(finalSessionKey);
        userSession.setOpenid(openid);
        String loginStatus = JSON.toJSONString(userSession);
        System.out.println(loginStatus);
        return loginStatus;
    }
}
