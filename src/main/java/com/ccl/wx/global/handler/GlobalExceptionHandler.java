package com.ccl.wx.global.handler;

import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.common.api.Result;
import com.ccl.wx.exception.*;
import com.ccl.wx.util.ResponseMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 褚超亮
 * @date 2020/3/2 17:23
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理前端传输的参数为空
     *
     * @param e
     * @return
     */
    @ExceptionHandler({MissingServletRequestParameterException.class, ParamIsNullException.class})
    public Result<String> requestMissingServletRequest(ParamIsNullException e) {
        log.error("参数为空！", e);
        return ResponseMsgUtil.exception(EnumResultCode.FAIL.getStatus(), e.getMessage(), e.getUrl());
    }

    /**
     * 处理用户不存在
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UserIsNullException.class)
    public Result<String> requestUserIsNull(UserIsNullException e) {
        log.error("用户不存在！", e);
        return ResponseMsgUtil.exception(EnumResultCode.USER_NONENTITY.getStatus(), e.getMessage(), e.getPath());
    }

    /**
     * 处理操作圈子为空
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CircleIsNullException.class)
    public Result<String> requestCircleIsNull(CircleIsNullException e) {
        log.error("操作圈子为空！", e);
        return ResponseMsgUtil.exception(EnumResultCode.CIRCLE_IS_NULL);
    }

    /**
     * 处理token为空
     *
     * @param e
     * @return
     */
    @ExceptionHandler(TokenIsNullException.class)
    public Result<String> requestTokenIsNull(TokenIsNullException e) {
        log.error("前端传输token为空");
        return ResponseMsgUtil.exception(EnumResultCode.USER_LOGIN_FAIL);
    }

    /**
     * 处理用户加入圈子异常
     * 用户不在此圈子中，或者用户被淘汰，退出圈子！！！
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UserJoinCircleException.class)
    public Result<String> requestUserJoinCircle(UserJoinCircleException e) {
        log.error("用户加入圈子异常！", e);
        return ResponseMsgUtil.exception(EnumResultCode.USER_JOIN_CIRCLE_NULL);
    }
}
