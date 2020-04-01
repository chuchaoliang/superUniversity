package com.ccl.wx.exception;

/**
 * @author 褚超亮
 * @date 2020/3/5 13:15
 */
public class UserIsNullException extends RuntimeException {

    private final String userId;

    private String path;

    public UserIsNullException(String userId, String path) {
        this.userId = userId;
        this.path = path;
    }

    public UserIsNullException(String userId) {
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return "查询不到此用户的相关信息:\'" + this.userId + "\'";
    }

    public String getPath() {
        return path;
    }

    public String getUserId() {
        return userId;
    }
}
