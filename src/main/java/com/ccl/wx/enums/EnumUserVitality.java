package com.ccl.wx.enums;

/**
 * @author 褚超亮
 * @date 2020/2/19 19:08
 */
public enum EnumUserVitality {
    /**
     * 用户连续签到所增加的活跃度（每连续签到7天）
     */
    USER_CONTINUOUS_CLOCK_IN(20),

    /**
     * 平时所增加的活跃度
     */
    USER_NO_CONTINUOUS_CLOCK_IN(5),

    /**
     * 积分周期，每几天多加积分
     */
    INTEGRATION_PERIOD(7);

    public Integer value;

    EnumUserVitality(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
