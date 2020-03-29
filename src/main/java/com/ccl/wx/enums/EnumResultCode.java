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
    INTERNAL_SERVER_ERROR(500),

    USER_LOGIN_FAIL(1000, "token为空，或者用户未登录"),

    USER_NONENTITY(1001, "登陆过期请重新登录，或者此用户不存在");

    private final int code;

    private String message;

    EnumResultCode(int code) {
        this.code = code;
    }

    EnumResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
