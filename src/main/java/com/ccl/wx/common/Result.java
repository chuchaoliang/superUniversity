package com.ccl.wx.common;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;

import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/3/2 17:52
 */
public class Result<T> {
    /**
     * 当前时间
     */
    private final String timestamp = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 错误的请求路径
     */
    private String path = "";

    /**
     * 返回数据
     */
    private T data;


    public Result() {
    }

    public Result(Integer status, String message, String path, T data) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.data = (T) JSON.parse((String) data);
    }

    public Result(Integer status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public Result(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        //this.data = data;
        this.data = (T) JSON.parse((String) data);
    }

    public Result(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "timestamp='" + timestamp + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                ", data=" + data +
                '}';
    }
}
