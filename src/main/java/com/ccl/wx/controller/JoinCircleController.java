package com.ccl.wx.controller;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.common.Result;
import com.ccl.wx.enums.EnumPage;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.util.ResponseMsgUtil;
import io.swagger.annotations.Api;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 褚超亮
 * @date 2020/3/16 22:04
 */
@Api(tags = {"JoinCircleController【用户加入圈子相关】"})
@RestController
@RequestMapping("/wx/circle")
public class JoinCircleController {

    @Resource
    private JoinCircleService joinCircleService;

    /**
     * 获取用户积分排行榜
     *
     * @param circleId 圈子id
     * @param page     当前页数
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/rank/all/vitality")
    public Result<String> getUserVitalityRanking(@RequestParam(value = "circleId", required = false) Long circleId,
                                                 @RequestParam(value = "page", required = false) Integer page) {
        String result = joinCircleService.getUserVitalityRankingData(circleId, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 获取用户连续打卡排行榜
     *
     * @param circleId 圈子id
     * @param page     当前页数
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/rank/all/signin")
    public Result<String> getUserSignInRanking(@RequestParam(value = "circleId", required = false) Long circleId,
                                               @RequestParam(value = "page", required = false) Integer page) {
        String result = joinCircleService.getUserSignInRankingData(circleId, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 获取某位用在圈子中的活跃度排名信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/rank/person/vitality")
    public Result<String> getPersonUserVitalityRanking(@RequestParam(value = "circleId", required = false) Long circleId,
                                                       @RequestHeader(value = "token", required = false) String userId) {
        List<Map> userVitalityRankingInfo = joinCircleService.getUserVitalityRankingInfo(circleId, userId, 0, EnumPage.LAST_NUMBER.getValue());
        return ResponseMsgUtil.success(JSON.toJSONString(userVitalityRankingInfo));
    }

    /**
     * 获取某位用在圈子中的连续打卡排名信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/rank/person/signin")
    public Result<String> getUserSignInRanking(@RequestParam(value = "circleId", required = false) Long circleId,
                                               @RequestHeader(value = "token", required = false) String userId) {
        List<Map> userSignInRankingInfo = joinCircleService.getUserSignInRankingInfo(circleId, userId, 0, EnumPage.LAST_NUMBER.getValue());
        return ResponseMsgUtil.success(JSON.toJSONString(userSignInRankingInfo));
    }

    /**
     * 根据日期获取用户打卡统计信息(已打卡)
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param date     时间(格式年月日)
     * @param page     第几页
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/statistics/all/success")
    public Result<String> getUserSignStatisticsSuccessInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                                           @RequestHeader(value = "token", required = false) String userId,
                                                           @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                                           @RequestParam(value = "page", required = false) Integer page) {
        String result = joinCircleService.getUserSignStatisticsSuccessInfo(circleId, userId, date, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 根据日期获取用户打卡统计信息（未打卡）
     * 参数含义同上
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param date     时间
     * @param page     第几页
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/statistics/all/fail")
    public Result<String> getUserSignStatisticsFailInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                                        @RequestHeader(value = "token", required = false) String userId,
                                                        @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                                        @RequestParam(value = "page", required = false) Integer page) {
        String result = joinCircleService.getUserSignStatisticsFailInfo(circleId, userId, date, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 根据日期获取打卡信息 已打卡人数，未打卡人数
     *
     * @param circleId 圈子id
     * @param date     日期 yyyyMMdd
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/statistics/number")
    public Result<String> getSignStatisticsNumberInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                                      @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        String result = joinCircleService.getCircleSignInInfo(circleId, date);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 加入圈子
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @ParamCheck
    @GetMapping("/join")
    public Result<String> joinCircle(@RequestParam(value = "circleId", required = false) Long circleId,
                                     @RequestHeader(value = "token", required = false) String userId) {
        String result = joinCircleService.joinCircle(circleId, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            ResponseMsgUtil.fail("加入圈子失败！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * TODO API
     * 根据密码加入圈子
     *
     * @param circleid  圈子id
     * @param userid    用户id
     * @param cpassword 圈子密码
     * @return
     */
    @GetMapping("/joinqzbyp")
    public String joinCircleByPassword(@RequestParam(value = "circleid", required = false) String circleid,
                                       @RequestParam(value = "userid", required = false) String userid,
                                       @RequestParam(value = "cpassword", required = false) String cpassword) {
        System.out.println("输入密码：" + cpassword);
        if (StringUtils.isEmpty(circleid) || StringUtils.isEmpty(userid) || StringUtils.isEmpty(cpassword)) {
            return "fail";
        } else {
            return "";
            //return circleService.joinPrivacyCircleByPassword(circleid, userid, cpassword);
        }
    }
}

