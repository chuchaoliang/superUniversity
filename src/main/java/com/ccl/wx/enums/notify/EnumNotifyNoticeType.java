package com.ccl.wx.enums.notify;

/**
 * @author 褚超亮
 * @date 2020/5/22 21:56
 */
public enum EnumNotifyNoticeType {
    // 全部
    ALL(0),
    // 部分
    PART(1),
    // 单独
    ONE(2);

    private int value;

    EnumNotifyNoticeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
