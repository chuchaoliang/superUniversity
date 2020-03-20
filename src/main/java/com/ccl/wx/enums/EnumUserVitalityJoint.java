package com.ccl.wx.enums;

/**
 * @author 褚超亮
 * @date 2020/2/19 17:09
 * 用户活跃度状态
 */
public enum EnumUserVitalityJoint {

    /**
     * 用户连续签到第7天 拼接字符串
     */
    USER_NO_CONTINUOUS_CLOCK_IN("->1"),

    /**
     * 其它 拼接字符串
     */
    USER_CONTINUOUS_CLOCK_IN("->0"),

    /**
     * 用户拼接字符串
     */
    USER_JOINT_STRING("->");

    public String value;

    EnumUserVitalityJoint(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
