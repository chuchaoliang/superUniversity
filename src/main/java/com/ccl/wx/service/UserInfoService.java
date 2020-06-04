package com.ccl.wx.service;

import com.ccl.wx.entity.UserInfo;

/**
 * @author 褚超亮
 * @date 2020/2/29 15:36
 */
public interface UserInfoService {

    int deleteByPrimaryKey(String id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    /**
     * 用户登录操作
     *
     * @param code
     * @param encryptedData
     * @param iv
     * @return
     */
    String userLogin(String code, String encryptedData, String iv);

    /**
     * 将session_key，openid字符串处理后 转为json对象返回
     *
     * @param sourceStr
     * @return
     */
    String encryptionSessionKey(String sourceStr);

    /**
     * 保存用户登陆信息
     *
     * @param userInfo 用户信息
     * @return
     */
    String saveUserInfo(UserInfo userInfo);
}



