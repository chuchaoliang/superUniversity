package com.ccl.wx.exception;

/**
 * @author 褚超亮
 * @date 2020/3/5 13:15
 */
public class CircleIsNullException extends RuntimeException {

    private final Long circleId;

    private final String url;

    public CircleIsNullException(Long circleId, String url) {
        this.circleId = circleId;
        this.url = url;
    }

    @Override
    public String getMessage() {
        return "查询不到此圈子的相关信息，此圈子不存在？:\'" + this.circleId + "\'";
    }

    public Long getCircleId() {
        return circleId;
    }

    public String getUrl() {
        return url;
    }
}
