package com.ccl.wx.controller;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.common.EnumResultCode;
import com.ccl.wx.common.Result;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.service.UserLikeService;
import com.ccl.wx.util.ResponseMsgUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 褚超亮
 * @date 2020/4/7 21:59
 */
@RestController
@RequestMapping("/wx/circle")
public class LikeController {

    @Resource
    private UserLikeService userLikeService;

    /**
     * 点赞
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @param diaryId  日志id
     * @return
     */
    @ParamCheck
    @GetMapping("/diary/like/add")
    public Result<String> saveLikeDiary(@RequestHeader(value = "token", required = false) String userId,
                                        @RequestParam(value = "circleId", required = false) String circleId,
                                        @RequestParam(value = "diaryId", required = false) String diaryId) {
        String result = userLikeService.saveLikeRedis(userId, circleId, diaryId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("点赞失败，操作频繁！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 取消点赞
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @param diaryId  日志id
     * @return
     */
    @ParamCheck
    @GetMapping("/diary/like/del")
    public Result<String> unLikeDiary(@RequestHeader(value = "token", required = false) String userId,
                                      @RequestParam(value = "circleId", required = false) String circleId,
                                      @RequestParam(value = "diaryId", required = false) String diaryId) {
        userLikeService.unLikeFromRedis(userId, circleId, diaryId);
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 获取日志某个日志的点赞信息
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @param diaryId  日志id
     * @return
     */
    @ParamCheck
    @GetMapping("/diary/like/get")
    public Result<String> getAllUserLike(@RequestHeader(value = "token", required = false) String userId,
                                         @RequestParam(value = "circleId", required = false) String circleId,
                                         @RequestParam(value = "diaryId", required = false) Long diaryId) {
        String result = JSON.toJSONStringWithDateFormat(userLikeService.getAllLikeUserNickName(userId, circleId, diaryId),
                DatePattern.NORM_DATETIME_MINUTE_PATTERN, SerializerFeature.DisableCircularReferenceDetect);
        return ResponseMsgUtil.success(result);
    }
}
