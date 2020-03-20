package com.ccl.wx.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.ccl.wx.common.Result;
import com.ccl.wx.enums.EnumResultCode;

import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/3/2 17:57
 */
public class ResponseMsgUtil {

    public static final String TIMESTAMP = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);

    /**
     * 根据消息码等生成接口返回对象
     *
     * @param code 结果返回码
     * @param msg  结果返回消息
     * @param <T>
     * @return
     */
    public static <T> Result<T> builderResponse(int code, String msg, String url) {
        Result<T> res = new Result<>();
        res.setTimestamp(TIMESTAMP);
        res.setStatus(code);
        res.setMessage(msg);
        res.setUrl(url);
        return res;
    }

    /**
     * 自定义处理异常
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> Result<T> builderResponse(int code, String msg) {
        Result<T> res = new Result<>();
        res.setTimestamp(TIMESTAMP);
        res.setStatus(code);
        res.setMessage(msg);
        return res;
    }

    /**
     * 请求异常返回结果
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> exception() {
        return builderResponse(EnumResultCode.INTERNAL_SERVER_ERROR.getCode(), "服务异常", null);
    }
}
