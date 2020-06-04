package com.ccl.wx.enums.notify;

/**
 * @author 褚超亮
 * @date 2020/5/22 21:56
 */
public enum EnumNotifyUserType {
    // 普通用户
    COMMON_USER(0),
    // 系统用户
    SYSTEM_USER(1);

    private int value;

    EnumNotifyUserType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
