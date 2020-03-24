package com.ccl.wx.controller;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.enums.EnumPage;
import com.ccl.wx.service.JoinCircleService;
import io.swagger.annotations.Api;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public String getUserVitalityRanking(@RequestParam(value = "circleId", required = false) Long circleId,
                                         @RequestParam(value = "page", required = false) Integer page) {
        return joinCircleService.getUserVitalityRankingData(circleId, page);
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
    public String getUserSignInRanking(@RequestParam(value = "circleId", required = false) Long circleId,
                                       @RequestParam(value = "page", required = false) Integer page) {
        return joinCircleService.getUserSignInRankingData(circleId, page);
    }

    /**
     * 获取某位用在圈子中的活跃度排名信息
     *
     * @param circleId
     * @param userId
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/rank/person/vitality")
    public String getPersonUserVitalityRanking(@RequestParam(value = "circleId", required = false) Long circleId,
                                               @RequestParam(value = "userId", required = false) String userId) {
        List<Map> userVitalityRankingInfo = joinCircleService.getUserVitalityRankingInfo(circleId, userId, 0, EnumPage.LAST_NUMBER.getValue());
        return JSON.toJSONString(userVitalityRankingInfo);
    }

    /**
     * 获取某位用在圈子中的连续打卡排名信息
     *
     * @param circleId
     * @param userId
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/rank/person/signin")
    public String getUserSignInRanking(@RequestParam(value = "circleId", required = false) Long circleId,
                                       @RequestParam(value = "userId", required = false) String userId) {
        List<Map> userSignInRankingInfo = joinCircleService.getUserSignInRankingInfo(circleId, userId, 0, EnumPage.LAST_NUMBER.getValue());
        return JSON.toJSONString(userSignInRankingInfo);
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
    public String getUserSignStatisticsSuccessInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                                   @RequestParam(value = "userId", required = false) String userId,
                                                   @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date date,
                                                   @RequestParam(value = "page", required = false) Integer page) {
        return joinCircleService.getUserSignStatisticsSuccessInfo(circleId, userId, date, page);
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
    public String getUserSignStatisticsFailInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                                @RequestParam(value = "userId", required = false) String userId,
                                                @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date date,
                                                @RequestParam(value = "page", required = false) Integer page) {
        return joinCircleService.getUserSignStatisticsFailInfo(circleId, userId, date, page);
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
    public String getSignStatisticsNumberInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                              @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date date) {
        return joinCircleService.getCircleSignInInfo(circleId, date);
    }
}

