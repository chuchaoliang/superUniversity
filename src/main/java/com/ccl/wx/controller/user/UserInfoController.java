package com.ccl.wx.controller.user;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.common.api.Result;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.global.annotation.ParamCheck;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.service.UserInfoService;
import com.ccl.wx.util.ResponseMsgUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author 褚超亮
 * @date 2019/12/5 15:59
 * 获取用户的相关信息
 */
@Api(tags = {"UserBasicController【用户的基本信息】"})
@Slf4j
@RestController
@RequestMapping("/person")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserDiaryService userDiaryService;

    ///**
    // * 获取用户的基本信息
    // *
    // * @param userid 用户id
    // * @return
    // */
    //@ApiOperation(value = "获取用户的基本信息", notes = "通过用户id获取用户基本信息")
    //@ApiImplicitParam(name = "userid", value = "用户ID", dataType = "String", example = "o1x2q5czO_xCH9eemeEfL41_gvMk")
    //@GetMapping("/getuserbasicinfo")
    //public String getUserBasicInfo(@ParamCheck @RequestParam(value = "userid", required = false) String userid) {
    //    UserBasicInfoDTO userBasicInfoDTO = new UserBasicInfoDTO();
    //    UserInfo userInfo = userInfoService.selectByPrimaryKey(userid);
    //    userBasicInfoDTO.setUserRank(userInfo.getUserId());
    //    userBasicInfoDTO.setSumClockNum(joinCircleMapper.sumUserSigninDayByUserid(userid));
    //    userBasicInfoDTO.setSumFoundCircle(joinCircleMapper.countByUserIdAndUserPermission(userid, 2));
    //    userBasicInfoDTO.setSumIntegral(joinCircleMapper.sumUserVitalityByUserid(userid));
    //    userBasicInfoDTO.setSumJoinCircle(joinCircleMapper.countByUserIdAndUserPermission(userid, 0));
    //    return JSON.toJSONString(userBasicInfoDTO);
    //}

    /**
     * 获取用户的信息
     *
     * @param userId 用户id
     * @return
     */
    @GetMapping("/user/get")
    public Result<String> getUserInfo(@ParamCheck @RequestParam(value = "userId", required = false) String userId) {
        return ResponseMsgUtil.success(JSON.toJSONStringWithDateFormat(userInfoService.selectByPrimaryKey(userId), DatePattern.NORM_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat));
    }

    /**
     * 获取登录态
     *
     * @param code 登录code 前端传输
     * @return
     */
    @ParamCheck
    @GetMapping("/user/login")
    public Result<String> userLogin(@RequestParam(value = "code", required = false) String code,
                                    @RequestParam(value = "encryptedData", required = false) String encryptedData,
                                    @RequestParam(value = "iv", required = false) String iv) {
        // 拼接url字符串
        String result = userInfoService.userLogin(code, encryptedData, iv);
        if (result.equals(EnumResultStatus.FAIL.getValue())) {
            // 登录失败
            return ResponseMsgUtil.fail(EnumResultCode.UNAUTHORIZED.getStatus(), "登录失败！");
        } else {
            return ResponseMsgUtil.success(result);
        }
    }

    /**
     * 插入用户数据，若重复则不进行插入，每次校检用户是否更改了数据
     *
     * @param userInfo 用户信息
     * @return
     */
    @Deprecated
    @PostMapping("/user/save")
    public Result userInfo(@Validated @RequestBody UserInfo userInfo, BindingResult result) {
        if (result.hasErrors()) {
            // 存在错误
            return ResponseMsgUtil.exception(EnumResultCode.FAIL.getStatus(), Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        } else {
            userInfoService.saveUserInfo(userInfo);
            return ResponseMsgUtil.success(JSON.toJSONString(userInfo));
        }
    }

    /**
     * 获取用户主页的日志动态消息
     *
     * @param userId 用户id
     * @param page   页数
     * @return
     */
    @ParamCheck
    @GetMapping("/home/diary")
    public Result<String> getUserHomeDiaryInfo(@RequestParam(value = "userId", required = false) String userId,
                                               @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        String userIndexDiaryInfo = userDiaryService.getUserIndexDiaryInfo(userId, page);
        return ResponseMsgUtil.success(userIndexDiaryInfo);
    }
}


