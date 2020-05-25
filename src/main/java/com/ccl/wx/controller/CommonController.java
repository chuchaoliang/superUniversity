package com.ccl.wx.controller;

import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.common.api.Result;
import com.ccl.wx.global.annotation.ParamCheck;
import com.ccl.wx.service.RedisService;
import com.ccl.wx.util.ResponseMsgUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 褚超亮
 * @date 2020/5/22 22:25
 */
@Api(tags = {"CommonController【公共接口】"})
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    private RedisService redisService;

    /**
     * 用户是否操作频繁
     *
     * @param userId 用户id
     * @param isHigh 是否为高频操作 默认false
     * @return
     */
    @GetMapping("/judge/click")
    public Result<String> judgeUserClick(@ParamCheck @RequestHeader(value = "token", required = false) String userId,
                                         @RequestParam(value = "high", required = false, defaultValue = "false") boolean isHigh) {
        if (redisService.judgeButtonClick(userId, isHigh)) {
            return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
        }
        return ResponseMsgUtil.fail(EnumResultCode.FAIL.getStatus(), "操作频率过高！！！");
    }
}
