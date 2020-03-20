package com.ccl.wx.enums;

/**
 * 代码所在环境
 * @author 褚超亮
 * @date 2020/3/5 11:04
 */
public enum EnumEnvironmentProfile {

    /**
     * 开发环境
     */
    LOCAL_PROFILE("dev"),

    /**
     * 正式环境
     */
    PROD_PROFILE("prod"),

    /**
     * 测试环境
     */
    TEST_PROFILE("test");

    private String value;

    EnumEnvironmentProfile(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

