package com.ccl.wx.util;

import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.common.api.IResultCode;
import com.ccl.wx.common.api.Result;

/**
 * @author 褚超亮
 * @date 2020/3/2 17:57
 */
public class ResponseMsgUtil {

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
     * 操作成功 无返回值
     *
     * @param iResultCode
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(IResultCode iResultCode) {
        return new Result<>(iResultCode.getStatus(), iResultCode.getMessage());
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
     * 失败请求
     *
     * @param iResultCode
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(IResultCode iResultCode) {
        return new Result<>(iResultCode.getStatus(), iResultCode.getMessage());
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
