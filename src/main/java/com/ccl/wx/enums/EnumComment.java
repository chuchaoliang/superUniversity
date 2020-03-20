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
    COMMENT_MASTER(1);

    public Integer value;

    EnumComment(Integer value) {
        this.value = value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
