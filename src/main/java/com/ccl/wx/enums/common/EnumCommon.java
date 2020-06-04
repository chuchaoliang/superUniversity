package com.ccl.wx.enums.common;

/**
 * @author 褚超亮
 * @date 2020/3/8 14:44
 */
public enum EnumCommon {
    // 字符串连接拼接符号
    STRING_JOINT(","),
    // 更新时的加值
    UPDATE_ADD(1),
    // 更新时候的减值
    UPDATE_SUB(-1),
    // 高频点击限制
    HIGH_CLICK(15),
    // 低频点击限制
    LOW_CLICK(5),
    // 未读
    NOT_READ(0),
    // 已读
    HAVE_READ(1),
    // 未删除
    NOT_DELETE(0),
    // 已删除
    HAVE_DELETE(1),
    // 系统通知目标用户id
    SYSTEM_NOTIFY("-1");

    private String value;

    private int data;

    EnumCommon(String value) {
        this.value = value;
    }

    EnumCommon(int data) {
        this.data = data;
    }

    public String getValue() {
        return value;
    }

    public int getData() {
        return data;
    }
}
