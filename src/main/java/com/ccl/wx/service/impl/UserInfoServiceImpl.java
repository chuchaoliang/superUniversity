package com.ccl.wx.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.config.properties.DefaultProperties;
import com.ccl.wx.config.properties.FileUploadProperties;
import com.ccl.wx.config.properties.FtpProperties;
import com.ccl.wx.config.properties.LoginData;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.mapper.UserInfoMapper;
import com.ccl.wx.pojo.UserSession;
import com.ccl.wx.service.UserInfoService;
import com.ccl.wx.util.CclUtil;
import com.ccl.wx.util.FtpUtil;
import com.ccl.wx.util.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

    @Resource
    private DefaultProperties defaultProperties;

    @Resource
    private FtpProperties ftpProperties;

    @Resource
    private FileUploadProperties fileUploadProperties;

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
    public String userLogin(String code, String encryptedData, String iv) {
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
        JSONObject login = JSON.parseObject(responseData);
        String openid = login.getString("openid");
        String sessionKey = login.getString("session_key");
        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(sessionKey)) {
            // 登录失败
            return EnumResultStatus.FAIL.getValue();
        }
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(openid);
        if (userInfo == null) {
            // 解密获取用户信息
            userInfo = JSON.parseObject(WechatUtil.decryptData(encryptedData, sessionKey, iv), UserInfo.class);
            if (userInfo == null) {
                // 登录失败
                return EnumResultStatus.FAIL.getValue();
            }
            // 设置openid
            userInfo.setId(openid);
            Integer maxUserId = userInfoMapper.selectMaxUserId();
            userInfo.setUid(CclUtil.getUid(StringUtils.isEmpty(maxUserId) ? 0 : maxUserId));
            String avatarUrl = userInfo.getAvatarurl();
            try {
                URL url = new URL(avatarUrl);
                InputStream stream = URLUtil.getStream(url);
                FastByteArrayOutputStream byteArrayOutputStream = IoUtil.read(stream);
                InputStream inputStreamType = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                // 获取文件类型
                String fileType = FileTypeUtil.getType(inputStreamType);
                if (StringUtils.isEmpty(fileType)) {
                    // 文件类型为空
                    userInfo.setAvatarurl(defaultProperties.getDefaultUserHeadImage());
                } else {
                    // 上传路径
                    String fileaddress = CclUtil.getFileUploadAddress(userInfo.getId()).replace("\\", "/");
                    String filename = CclUtil.getFileUploadName(fileType);
                    InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    // 上传文件
                    boolean upload = FtpUtil.upload(CclUtil.delStringPrefix(fileaddress, ftpProperties.getBasePath()), filename, inputStream);
                    log.info("文件名：" + filename);
                    log.info("文件地址：" + fileaddress);
                    if (upload) {
                        // 上传成功
                        String httpPath = ftpProperties.getHttpPath();
                        log.info("文件：【" + filename + "】上传成功");
                        userInfo.setAvatarurl(httpPath + CclUtil.delStringPrefix(fileaddress, fileUploadProperties.getUselessLocalPath()) + filename);
                    } else {
                        // 上传失败 使用默认图片
                        log.info("文件：【" + filename + "】上传失败");
                        userInfo.setAvatarurl(defaultProperties.getDefaultUserHeadImage());
                    }
                }
                userInfoMapper.insertSelective(userInfo);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return JSON.toJSONStringWithDateFormat(userInfo, DatePattern.NORM_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
    }

    @Override
    public String encryptionSessionKey(String sourceStr) {
        JSONObject strJson = JSONObject.parseObject(sourceStr);
        String sessionKey = strJson.getString("session_key");
        String openid = strJson.getString("openid");
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


