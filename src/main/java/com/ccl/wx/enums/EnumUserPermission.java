package com.ccl.wx.enums;

/**
 * @author 褚超亮
 * @date 2020/3/27 15:30
 */
public enum EnumUserPermission {

    /**
     * 普通用户
     */
    ORDINARY_USER(0),

    /**
     * 管理员用户
     */
    ADMIN_USER(1),

    /**
     * 圈主用户
     */
    MASTER_USER(2);

    private Integer value;

    EnumUserPermission(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
