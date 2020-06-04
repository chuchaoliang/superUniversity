package com.ccl.wx.enums.user;

/**
 * @author 褚超亮
 * @date 2020/6/4 15:32
 */
public enum EnumUserInfo {
    // 用户正常状态
    USER_NORMAL(0),
    // 用户异常状态
    USER_EXCEPTION(1);
    private int value;

    EnumUserInfo(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
