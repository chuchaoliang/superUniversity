package com.ccl.wx.common;

/**
 * @author 褚超亮
 * @date 2020/3/2 17:54
 */
public enum EnumResultCode implements IResultCode {

    SUCCESS(200, "请求成功"),

    FAIL(400, "部分请求参数为空"),

    UNAUTHORIZED(401, "无操作权限"),

    NOT_FOUND(404, "请求资源不存在啊！！"),

    INTERNAL_SERVER_ERROR(500, "服务器出现错误了!"),

    USER_LOGIN_FAIL(1000, "token为空，或者用户未登录！"),

    USER_NONENTITY(1001, "登陆过期请重新登录，或者此用户不存在！"),

    CIRCLE_IS_NULL(1002, "所操作的圈子不存在哦!"),

    USER_JOIN_CIRCLE_NULL(1003, "用户不存在此圈子中，或者被淘汰！"),

    THEME_IS_NULL(1004, "此主题已经过期，或者不存在了！"),

    CIRCLE_NAME_REPETITION(1005, "圈子昵称重复！");

    private final int status;

    private String message;

    private EnumResultCode(int status) {
        this.status = status;
    }

    EnumResultCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
