package com.ccl.wx.enums.diary;

/**
 * @author 褚超亮
 * @date 2020/3/25 19:19
 */
public enum EnumLike {

    /**
     * 点赞状态
     */
    LIKE_SUCCESS(1),

    /**
     * 取消点赞状态
     */
    LIKE_FAIL(0),

    /**
     * 日志点赞
     */
    LIKE_DIARY(1),

    /**
     * 评论点赞
     */
    LIKE_COMMENT(2),

    /**
     * 回复点赞
     */
    LIKE_REPLY(3),

    /**
     * 一段时间内总点赞数
     */
    SUM_LIKE(15),

    /**
     * 过期时间（s）
     */
    OUT_TIME(60),

    /**
     * 日记展示的全部点赞
     */
    USER_LIKE_NUMBER(10);

    private Integer value;

    EnumLike(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
