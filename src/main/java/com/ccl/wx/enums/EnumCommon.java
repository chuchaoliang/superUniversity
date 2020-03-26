package com.ccl.wx.enums;

/**
 * @author 褚超亮
 * @date 2020/3/8 14:44
 */
public enum EnumCommon {
    /**
     * 字符串连接拼接符号
     */
    STRING_JOINT(","),

    /**
     * 更新时的加值
     */
    UPDATE_ADD("1"),

    /**
     * 更新时候的减值
     */
    UPDATE_SUB("-1");

    private String value;

    EnumCommon(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
