package com.ccl.wx.enums.circle;

/**
 * @author 褚超亮
 * @date 2020/4/9 9:37
 */
public enum EnumCircle {
    /**
     * 直接加入
     */
    DIRECT_JOIN(0),

    /**
     * 需要同意加入
     */
    AGREE_JOIN(1),

    /**
     * 输入密码加入（私密圈子）
     */
    PASSWORD_JOIN(2),

    /**
     * 密码长度
     */
    PASSWORD_LENGTH(6),

    /**
     * 圈子中最大管理员个数
     */
    ADMIN_MAX_NUMBER(10);

    private int value;

    EnumCircle(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
