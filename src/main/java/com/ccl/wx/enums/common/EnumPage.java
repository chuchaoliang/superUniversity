package com.ccl.wx.enums.common;

/**
 * @author 褚超亮
 * @date 2020/3/12 21:55
 */
public enum EnumPage {

    /**
     * 每页的数量
     */
    PAGE_NUMBER(10),

    /**
     * 圈子主页展示的主题总数
     */
    CIRCLE_HOME_THEME(5),

    /**
     * 作用于用户排名，只计算前200名，如果超过200名，变成9999
     */
    LAST_NUMBER(200);


    private int value;

    EnumPage(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
