package com.ccl.wx.enums.notify;

/**
 * 通知的资源对应的类型
 *
 * @author 褚超亮
 * @date 2020/5/22 19:04
 */
public enum EnumNotifyResourceType {
    // 日志
    DIARY(0),
    // 用户
    USER(1),
    // 通知
    INFORM(2),
    // 私信
    CHAT(3);
    private int value;

    EnumNotifyResourceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
