package com.ccl.wx.enums;

/**
 * @author 褚超亮
 * @date 2020/3/8 14:44
 */
public enum EnumCommon {
    /**
     * 字符串连接拼接符号
     */
    STRING_JOINT(",");

    public String value;

    EnumCommon(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
