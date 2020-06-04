package com.ccl.wx.controller.notify;

import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.common.api.Result;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.service.SystemNotifyService;
import com.ccl.wx.util.ResponseMsgUtil;
import com.ccl.wx.vo.notify.SystemNotifyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author 褚超亮
 * @date 2020/6/3 22:31
 */
@Api(tags = {"SystemNotifyController【系统消息处理】"})
@Slf4j
@RestController
@RequestMapping("/notify")
public class SystemNotifyController {

    @Resource
    private SystemNotifyService systemNotifyService;

    // TODO 普通用户通知待存在bug
    @ApiOperation(value = "发送消息通知")
    @PostMapping("/send")
    public Result<String> sendSystemAndUserNotify(@Validated @RequestBody SystemNotifyVO systemNotifyVO,
                                                  @RequestParam(value = "type", required = false, defaultValue = "0") Integer type,
                                                  BindingResult result) {
        if (result.hasErrors()) {
            return ResponseMsgUtil.fail(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        String responseResult = systemNotifyService.sendMessageNotify(systemNotifyVO, type);
        if (EnumResultStatus.FAIL.getValue().equals(responseResult)) {
            return ResponseMsgUtil.fail("发送人用户不存在或者已经被移除！！！");
        } else if (EnumResultStatus.UNKNOWN.getValue().equals(responseResult)) {
            return ResponseMsgUtil.fail("消息发送失败！！！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }
}
