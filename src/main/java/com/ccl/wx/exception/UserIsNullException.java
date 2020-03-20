package com.ccl.wx.exception;

/**
 * @author 褚超亮
 * @date 2020/3/5 13:15
 */
public class UserIsNullException extends RuntimeException {

    private final String userId;

    private final String url;

    public UserIsNullException(String userId, String url) {
        super("");
        this.userId = userId;
        this.url = url;
    }

    @Override
    public String getMessage() {
        return "查询不到此用户的相关信息:\'" + this.userId + "\'";
    }

    public String getUrl() {
        return url;
    }

    public String getUserId() {
        return userId;
    }
}
