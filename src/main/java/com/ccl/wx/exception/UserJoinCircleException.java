package com.ccl.wx.exception;

/**
 * @author 褚超亮
 * @date 2020/3/18 19:55
 */
public class UserJoinCircleException extends RuntimeException {

    public UserJoinCircleException(String message) {
        super(message);
    }

    public UserJoinCircleException(String message, Throwable cause) {
        super(message, cause);
    }
}
