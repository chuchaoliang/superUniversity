package com.ccl.wx.enums.circle;

/**
 * @author 褚超亮
 * @date 2020/3/6 14:27
 */
public enum EnumThemeStatus {

    /**
     * 主题正常状态
     */
    USE_STATUS(0),

    /**
     * 主题删除状态
     */
    DELETE_STATUS(1);

    private int value;

    EnumThemeStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
