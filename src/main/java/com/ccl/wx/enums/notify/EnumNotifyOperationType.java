package com.ccl.wx.enums.notify;

/**
 * 消息操作类型
 *
 * @author 褚超亮
 * @date 2020/5/24 16:09
 */
public enum EnumNotifyOperationType {
    // 正常
    NORMAL(0),
    // 不提醒，但持久化
    NO_REMIND(1),
    // 不持久化，更不提醒
    NO_PERSISTENCE(2);

    private int value;

    EnumNotifyOperationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
