package com.ccl.wx.controller;

import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.mapper.UserInfoMapper;
import com.ccl.wx.pojo.LoginData;
import com.ccl.wx.service.impl.LoginServiceImpl;
import com.ccl.wx.util.CclUtil;
import com.ccl.wx.util.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;


/**
 * @author CCL
 * @date 2019/10/2 18:46
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/wx")
public class LoginController {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private LoginData login;

    /**
     * 获取登录态
     *
     * @param code
     * @return
     */
    @GetMapping("/login")
    public String userLogin(@RequestParam(value = "code", required = false) String code) {
        // 拼接url字符串
        if (StringUtils.isEmpty(code)) {
            return "fail";
        } else {
            log.info("用户登录操作code ：" + code);
            String wxUrl = "https://api.weixin.qq.com/sns/jscode2session";
            StringBuilder wxUrlBuilder = new StringBuilder(wxUrl);
            wxUrlBuilder.append("?appid=" + login.getAppId());
            wxUrlBuilder.append("&secret=" + login.getSecret());
            wxUrlBuilder.append("&js_code=" + code);
            wxUrlBuilder.append("&grant_type=authorization_code");
            // 发送get请求获得session_key,openid
            RestTemplate restTemplate = new RestTemplate();
            String loginData = restTemplate.getForObject(new String(wxUrlBuilder), String.class);
            String loginStatus = loginService.encryptionSessionKey(loginData);
            log.info("用户登录成功SUCCESS：" + loginStatus);
            return loginStatus;
        }
    }

    /**
     * 插入用户数据，若重复则不进行插入，每次校检用户是否更改了数据
     *
     * @param userInfo
     * @returnw
     */
    @PostMapping("/userinfo")
    public String setUserInfo(@Validated(UserInfo.Default.class) @RequestBody UserInfo userInfo) {
        UserInfo user = userInfoMapper.findByOpenid(userInfo.getId());
        if (user != null) {
            //用户曾经登陆过，检测用户信息是否改变，若改变更新信息
            ArrayList<String> ignoreStr = new ArrayList<>();
            ignoreStr.add("createtime");
            ignoreStr.add("updatetime");
            boolean strJudge = CclUtil.compareObjectAttribute(userInfo, user, ignoreStr);
            if (!strJudge) {
                Date updatetime = new Date();
                user.setUpdatetime(updatetime);
                userInfoMapper.updateByPrimaryKeySelective(userInfo);
            }
        } else {
            //用户第一次登录，直接插入
            userInfoMapper.insertSelective(userInfo);
        }
        return "success";
    }

    /**
     * TODO API
     * 上传图片
     *
     * @param image
     * @param userid
     * @return
     */
    @PostMapping("/uploadimage")
    public String saveImage(@RequestPart(value = "image", required = false) MultipartFile image,
                            @RequestParam(value = "userid", required = false) String userid) {
        if (image == null || StringUtils.isEmpty(userid)) {
            return "fail";
        } else {
            FtpUtil.uploadFile(userid, image);
        }
        return "success";
    }
}
