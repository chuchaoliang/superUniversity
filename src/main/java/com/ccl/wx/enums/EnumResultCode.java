package com.ccl.wx.enums;

/**
 * @author 褚超亮
 * @date 2020/3/2 17:54
 */
public enum EnumResultCode {
    /**
     * 成功
     */
    SUCCESS(200),

    /**
     * 失败，参数值为空
     */
    FAIL(400),

    /**
     * 未授权，用户不存在
     */
    UNAUTHORIZED(401),

    /**
     * 不存在
     */
    NOT_FOUND(404),

    /**
     * 出现错误
     */
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    EnumResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
