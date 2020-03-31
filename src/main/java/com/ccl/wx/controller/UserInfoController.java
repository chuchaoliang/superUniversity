package com.ccl.wx.controller;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.dto.UserBasicInfoDTO;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.mapper.JoinCircleMapper;
import com.ccl.wx.service.CircleService;
import com.ccl.wx.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 褚超亮
 * @date 2019/12/5 15:59
 * 获取用户的相关信息
 */
@Api(tags = {"UserBasicController【用户的基本信息】"})
@Slf4j
@RestController
@RequestMapping("/wx/person")
public class UserInfoController {

    @Resource
    private JoinCircleMapper joinCircleMapper;

    @Resource
    private CircleService circleService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 获取用户的基本信息
     *
     * @param userid 用户id
     * @return
     */
    @ApiOperation(value = "获取用户的基本信息", notes = "通过用户id获取用户基本信息")
    @ApiImplicitParam(name = "userid", value = "用户ID", dataType = "String", example = "o1x2q5czO_xCH9eemeEfL41_gvMk")
    @GetMapping("/getuserbasicinfo")
    public String getUserBasicInfo(@ParamCheck @RequestParam(value = "userid", required = false) String userid) {
        UserBasicInfoDTO userBasicInfoDTO = new UserBasicInfoDTO();
        UserInfo userInfo = userInfoService.selectByPrimaryKey(userid);
        userBasicInfoDTO.setUserRank(userInfo.getUserId());
        userBasicInfoDTO.setSumClockNum(joinCircleMapper.sumUserSigninDayByUserid(userid));
        userBasicInfoDTO.setSumFoundCircle(joinCircleMapper.countByUserIdAndUserPermission(userid, 2));
        userBasicInfoDTO.setSumIntegral(joinCircleMapper.sumUserVitalityByUserid(userid));
        userBasicInfoDTO.setSumJoinCircle(joinCircleMapper.countByUserIdAndUserPermission(userid, 0));
        return JSON.toJSONString(userBasicInfoDTO);
    }

    /**
     * 获取用户的积分
     *
     * @return
     */
    @ApiOperation(value = "获取用户的能量球（积分）", notes = "根据用户的id获取用户的能量球")
    @ApiImplicitParam(name = "userid", value = "用户id", dataType = "String", example = "o1x2q5czO_xCH9eemeEfL41_gvMk")
    @GetMapping("/getuserintegral")
    public String getUserIntegral(@RequestParam(value = "userid", required = false) String userid) {
        int integral = joinCircleMapper.sumUserVitalityByUserid(userid);
        return String.valueOf(integral);
    }


    /**
     * 获取用户在圈子中的信息
     *
     * @param userid   用户id
     * @param circleid 圈子id
     * @return
     */
    @ApiOperation(value = "获取用户加入圈子的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "circleid", value = "圈子id", dataType = "Long", example = "5")
    })
    @GetMapping("/getuserincircleinfo")
    public String test(@RequestParam(value = "userid", required = false) String userid,
                       @RequestParam(value = "circleid", required = false) Long circleid) {
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(circleid)) {
            return "fail";
        } else {
            return circleService.getUserInCircleInfo(userid, circleid);
        }
    }

    /**
     * 根据用户id 判断用户是否不存在,用户存在success 不存在fail
     *
     * @param id 用户id
     * @return
     */
    @ApiOperation(value = "判断此用户是否为空")
    @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String")
    @GetMapping("/judgeuserinfo")
    public String judgeUserInfoIsNull(@ParamCheck @RequestParam(value = "userId", required = false) String id) {
        UserInfo userInfo = userInfoService.selectByPrimaryKey(id);
        if (userInfo == null) {
            return EnumResultStatus.FAIL.getValue();
        }
        return EnumResultStatus.SUCCESS.getValue();
    }

    /**
     * 获取登录态
     *
     * @param code 登录code 前端传输
     * @return
     */
    @GetMapping("/user/login")
    public String userLogin(@RequestParam(value = "code", required = false) String code) {
        // 拼接url字符串
        return userInfoService.userLogin(code);
    }

    /**
     * 插入用户数据，若重复则不进行插入，每次校检用户是否更改了数据
     *
     * @param userInfo 用户信息
     * @return
     */
    @PostMapping("/user/save")
    public String userInfo(@Validated(UserInfo.Default.class) @RequestBody UserInfo userInfo) {
        return userInfoService.saveUserInfo(userInfo);
    }
}


