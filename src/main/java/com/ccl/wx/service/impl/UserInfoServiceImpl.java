package com.ccl.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.mapper.UserInfoMapper;
import com.ccl.wx.pojo.LoginData;
import com.ccl.wx.pojo.UserSession;
import com.ccl.wx.service.UserInfoService;
import com.ccl.wx.util.CclUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/2/29 15:37
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private LoginData loginData;

    @Override
    public int deleteByPrimaryKey(String id) {
        return userInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserInfo record) {
        return userInfoMapper.insert(record);
    }

    @Override
    public int insertSelective(UserInfo record) {
        return userInfoMapper.insertSelective(record);
    }

    @Override
    public UserInfo selectByPrimaryKey(String id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserInfo record) {
        return userInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserInfo record) {
        return userInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public String userLogin(String code) {
        // 拼接url字符串
        log.info("用户登录操作code ：" + code);
        String wxLoginUrl = "https://api.weixin.qq.com/sns/jscode2session";
        StringBuilder loginStringBuilder = new StringBuilder(wxLoginUrl)
                .append("?appid=").append(loginData.getAppId())
                .append("&secret=").append(loginData.getSecret())
                .append("&js_code=").append(code)
                .append("&grant_type=authorization_code");
        // 发送get请求获得session_key,openid
        RestTemplate restTemplate = new RestTemplate();
        String responseData = restTemplate.getForObject(new String(loginStringBuilder), String.class);
        String loginStatus = encryptionSessionKey(responseData);
        log.info("用户登录!!" + loginStatus);
        return loginStatus;
    }

    @Override
    public String encryptionSessionKey(String sourceStr) {
        JSONObject strJson = JSONObject.parseObject(sourceStr);
        String sessionKey = strJson.getString("session_key");
        String openid = strJson.getString("openid");
        // TODO 获取session_key的hash值 可以重构用更好的的方式处理。。。
        if (StringUtils.isEmpty(sessionKey) || StringUtils.isEmpty(openid)) {
            return EnumResultStatus.FAIL.getValue();
        }
        String finalSessionKey = Integer.toHexString(sessionKey.hashCode());
        UserSession userSession = new UserSession();
        userSession.setEncryptSessionKey(finalSessionKey);
        userSession.setOpenid(openid);
        return JSON.toJSONString(userSession);
    }

    @Override
    public String saveUserInfo(UserInfo userInfo) {
        UserInfo user = userInfoMapper.selectByPrimaryKey(userInfo.getId());
        if (user != null) {
            //用户曾经登陆过，检测用户信息是否改变，若改变更新信息
            ArrayList<String> ignoreStr = new ArrayList<>();
            ignoreStr.add("createtime");
            ignoreStr.add("updatetime");
            boolean strJudge = CclUtil.compareObjectAttribute(userInfo, user, ignoreStr);
            if (!strJudge) {
                user.setUpdatetime(new Date());
                userInfoMapper.updateByPrimaryKeySelective(userInfo);
            }
        } else {
            //用户第一次登录，直接插入
            userInfoMapper.insertSelective(userInfo);
        }
        return EnumResultStatus.SUCCESS.getValue();
    }
}
