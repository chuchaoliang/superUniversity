package com.ccl.wx.enums.circle;

/**
 * @author 褚超亮
 * @date 2020/2/19 16:14
 * 用户打卡状态
 */
public enum EnumUserClockIn {

    /**
     * 用户未打卡状态
     */
    USER_CLOCK_IN_FAIL(0),

    /**
     * 用户打卡状态
     */
    USER_CLOCK_IN_SUCCESS(1),

    /**
     * 用户今日完成全部主题打卡状态
     */
    USER_ALL_CLOCK_IN_SUCCESS(2);


    public int value;

    EnumUserClockIn(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
