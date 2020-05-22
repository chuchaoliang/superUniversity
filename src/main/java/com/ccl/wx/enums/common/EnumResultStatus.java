package com.ccl.wx.enums.common;

/**
 * @author 褚超亮
 * @date 2020/3/3 13:51
 */
public enum EnumResultStatus {

    /**
     * 请求成功
     */
    SUCCESS("success"),

    /**
     * 请求失败
     */
    FAIL("fail"),

    /**
     * 部分参数为空
     */
    PARAMS_NULL("-1"),

    /**
     * 未知错误
     */
    UNKNOWN("1"),

    /**
     * 无意义参数
     */
    UNMEANING("null");

    private String value;

    EnumResultStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
