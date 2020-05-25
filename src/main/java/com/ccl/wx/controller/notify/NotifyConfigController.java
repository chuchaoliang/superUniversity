package com.ccl.wx.controller.notify;

import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.common.api.Result;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.global.annotation.ParamCheck;
import com.ccl.wx.service.NotifyConfigService;
import com.ccl.wx.util.ResponseMsgUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/5/25 15:50
 */
@Api(tags = {"NotifyConfigController【用户通知配置】"})
@Slf4j
@RestController
@RequestMapping("/notify")
public class NotifyConfigController {

    @Resource
    private NotifyConfigService notifyConfigService;

    @GetMapping("/user/config/add")
    public Result<String> userAddConfig(@ParamCheck @RequestParam(value = "configs", required = false) List<Integer> configs,
                                        @RequestHeader(value = "token", required = false) String userId) {
        String result = notifyConfigService.addUserNotifyConfig(configs, userId);
        if (result.equals(EnumResultStatus.SUCCESS.getValue())) {
            return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
        }
        return ResponseMsgUtil.fail("操作失败");
    }

    @GetMapping("/user/config/del")
    public Result<String> userDelConfig(@RequestParam(value = "configs", required = false) List<Integer> configs,
                                        @RequestHeader(value = "token", required = false) String userId,
                                        @RequestParam(value = "type", required = false, defaultValue = "1") Integer type) {
        String result = notifyConfigService.delUserNotifyConfig(configs, userId, type);
        if (result.equals(EnumResultStatus.SUCCESS.getValue())) {
            return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
        }
        return ResponseMsgUtil.fail("操作失败");
    }
}
