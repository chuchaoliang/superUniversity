package com.ccl.wx.enums.circle;

/**
 * @author 褚超亮
 * @date 2020/2/22 12:00
 */
public enum EnumUserCircle {

    /**
     * 用户正常状态
     */
    USER_NORMAL_STATUS(0),

    /**
     * 用户待加入状态
     */
    USER_AWAIT_STATUS(1),

    /**
     * 用户淘汰状态
     */
    USER_OUT_STATUS(2),

    /**
     * 用户被拒绝状态
     */
    USER_REFUSE_STATUS(3),

    /**
     * 用户退出圈子
     */
    USER_EXIT_STATUS(4);

    private int value;

    EnumUserCircle(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
