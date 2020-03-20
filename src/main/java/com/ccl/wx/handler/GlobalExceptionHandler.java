package com.ccl.wx.handler;

import com.ccl.wx.common.Result;
import com.ccl.wx.enums.EnumResultCode;
import com.ccl.wx.exception.ParamIsNullException;
import com.ccl.wx.exception.UserIsNullException;
import com.ccl.wx.exception.UserJoinCircleException;
import com.ccl.wx.util.ResponseMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

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
        return ResponseMsgUtil.builderResponse(EnumResultCode.FAIL.getCode(), e.getMessage(), e.getUrl());
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
        return ResponseMsgUtil.builderResponse(EnumResultCode.UNAUTHORIZED.getCode(), e.getMessage(), e.getUrl());
    }

    /**
     * 处理用户加入圈子异常
     * 用户不在此圈子中，或者用户被淘汰，退出圈子！！！
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(UserJoinCircleException.class)
    public Result<String> requestUserJoinCircle(UserJoinCircleException e, HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        log.error("用户加入圈子异常！", e);
        return ResponseMsgUtil.builderResponse(EnumResultCode.FAIL.getCode(), e.getMessage(), url);
    }
}
