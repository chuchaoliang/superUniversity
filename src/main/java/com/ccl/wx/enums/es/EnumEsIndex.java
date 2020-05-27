package com.ccl.wx.enums.es;

/**
 * @author 褚超亮
 * @date 2020/4/26 20:32
 */
public enum EnumEsIndex {
    /**
     * es中用户信息索引
     */
    ES_USER_INFO("user"),
    /**
     * es中圈子信息索引
     */
    ES_CIRCLE_INFO("circle");

    private String value;

    EnumEsIndex(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
