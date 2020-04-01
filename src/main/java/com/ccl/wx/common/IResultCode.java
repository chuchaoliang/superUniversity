package com.ccl.wx.common;

/**
 * @author 褚超亮
 * @date 2020/3/31 11:51
 */
public interface IResultCode {

    /**
     * 获取状态码
     * @return
     */
    Integer getStatus();

    /**
     * 获取状态消息
     * @return
     */
    String getMessage();
}
