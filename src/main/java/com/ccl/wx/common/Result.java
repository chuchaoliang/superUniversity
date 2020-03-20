package com.ccl.wx.common;

/**
 * @author 褚超亮
 * @date 2020/3/2 17:52
 */
public class Result<T> {
    /**
     * 当前时间
     */
    public String timestamp;

    /**
     * 状态码
     */
    public Integer status;

    /**
     * 错误信息
     */
    public String message;

    /**
     * 错误的请求路径
     */
    public String url;

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
