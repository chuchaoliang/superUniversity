package com.ccl.wx.exception;

/**
 * @author 褚超亮
 * @date 2020/3/5 13:15
 */
public class CircleIsNullException extends RuntimeException {

    private final Long circleId;

    private String path;

    public CircleIsNullException(Long circleId, String path) {
        this.circleId = circleId;
        this.path = path;
    }

    public CircleIsNullException(Long circleId) {
        this.circleId = circleId;
    }

    @Override
    public String getMessage() {
        return "查询不到此圈子的相关信息，此圈子不存在？:\'" + this.circleId + "\'";
    }

    public Long getCircleId() {
        return circleId;
    }

    public String getPath() {
        return path;
    }
}
