package com.ccl.wx.exception;

/**
 * @author 褚超亮
 * @date 2020/3/2 17:01
 */
public class ParamIsNullException extends RuntimeException {

    private final String parameterName;

    private final String parameterType;

    private final String url;

    public ParamIsNullException(String parameterName, String parameterType, String url) {
        super("");
        this.parameterName = parameterName;
        this.parameterType = parameterType;
        this.url = url;
    }

    @Override
    public String getMessage() {
        return "参数类型:" + this.parameterType + " 参数值名称\'" + this.parameterName + "\' 不能为空！";
    }

    public final String getParameterName() {
        return this.parameterName;
    }

    public final String getParameterType() {
        return this.parameterType;
    }

    public String getUrl() {
        return url;
    }
}
