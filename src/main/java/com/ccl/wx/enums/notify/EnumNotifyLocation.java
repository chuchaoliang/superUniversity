package com.ccl.wx.enums.notify;

/**
 * 通知所在的位置
 *
 * @author 褚超亮
 * @date 2020/5/22 18:57
 */
public enum EnumNotifyLocation {
    // 通知（4,5,6,7,8,9,10,11,13）
    INFORM(0),
    // 评论（1,3）
    COMMON_COMMENT(1),
    // 点评（2）
    COMMENT(2),
    // 点赞（0）
    LIKE(3);

    private int value;

    EnumNotifyLocation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
