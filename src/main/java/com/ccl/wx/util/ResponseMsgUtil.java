package com.ccl.wx.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.ccl.wx.common.IResultCode;
import com.ccl.wx.common.Result;
import com.ccl.wx.common.EnumResultCode;

import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/3/2 17:57
 */
public class ResponseMsgUtil {

    public static final String TIMESTAMP = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);

    private ResponseMsgUtil() {
    }

    /**
     * 返回成功
     *
     * @param status
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(int status, String message, T data) {
        return new Result<>(status, message, data);
    }

    /**
     * 成功返回数据
     *
     * @param iResultCode
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(IResultCode iResultCode, T data) {
        return new Result<>(iResultCode.getStatus(), iResultCode.getMessage(), data);
    }

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(EnumResultCode.SUCCESS.getStatus(), EnumResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * @param status
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(int status, String message) {
        return new Result<>(status, message);
    }

    /**
     * 失败请求
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(EnumResultCode.FAIL.getStatus(), message);
    }

    /**
     * 自定义处理异常
     *
     * @param status
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> exception(int status, String message) {
        return new Result<>(status, message);
    }

    /**
     * 自定义处理异常
     *
     * @param iResultCode
     * @param <T>
     * @return
     */
    public static <T> Result<T> exception(IResultCode iResultCode) {
        return new Result<>(iResultCode.getStatus(), iResultCode.getMessage());
    }

    /**
     * 自定义异常处理
     *
     * @param status  结果返回码
     * @param message 结果返回消息
     * @param <T>
     * @return
     */
    public static <T> Result<T> exception(int status, String message, String path) {
        return new Result<>(status, message, path);
    }
}
