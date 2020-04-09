package com.ccl.wx.enums;

/**
 * 用户评论类型
 *
 * @author 褚超亮
 * @date 2020/2/29 11:46
 */
public enum EnumComment {

    /**
     * 普通用户评论
     */
    COMMENT_USER(0),

    /**
     * 圈主评论
     */
    COMMENT_MASTER(1),

    /**
     * 评论正常状态
     */
    COMMENT_NORMAL(0),

    /**
     * 评论删除状态
     */
    COMMENT_DELETE_STATUS(1),

    /**
     * 日记点评最多数量
     */
    COMMENT_MAX_NUMBER(3);

    public Integer value;

    EnumComment(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
