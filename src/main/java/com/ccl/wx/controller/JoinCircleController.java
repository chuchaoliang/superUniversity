package com.ccl.wx.controller;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.enums.EnumPage;
import com.ccl.wx.service.JoinCircleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
}

