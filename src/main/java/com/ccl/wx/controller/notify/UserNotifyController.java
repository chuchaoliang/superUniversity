package com.ccl.wx.controller.notify;

import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.common.api.Result;
import com.ccl.wx.global.annotation.ParamCheck;
import com.ccl.wx.service.UserNotifyService;
import com.ccl.wx.util.ResponseMsgUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 褚超亮
 * @date 2020/6/5 17:26
 */
@Api(tags = {"UserNotifyController【用户消息处理】"})
@Slf4j
@RestController
@RequestMapping("/notify")
public class UserNotifyController {

    @Resource
    private UserNotifyService userNotifyService;

    /**
     * 获取用户某一位置的消息
     *
     * @param location 位置
     * @param userId   用户id
     * @param page     第几页
     * @return
     */
    @GetMapping("/user/get")
    public Result<String> getUserLocationNotify(@ParamCheck @RequestParam(value = "location", required = false) Integer location,
                                                @RequestHeader(value = "token", required = false) String userId,
                                                @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        String result = userNotifyService.getUserLocationNotify(location, userId, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 获取用户某一位置未读消息数量
     *
     * @param location 位置
     * @return
     */
    @GetMapping("/user/get/number")
    public Result<String> getUserLocationNotifyNumber(@RequestParam(value = "location", required = false) Integer location) {
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }
}
