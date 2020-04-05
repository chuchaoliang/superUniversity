package com.ccl.wx.exception;

/**
 * @author 褚超亮
 * @date 2020/3/31 10:51
 */
public class TokenIsNullException extends RuntimeException {

    private final Integer code;

    private final String message;

    private String path;

    public TokenIsNullException(String path, Integer code, String message) {
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public TokenIsNullException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
