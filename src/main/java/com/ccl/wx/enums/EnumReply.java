package com.ccl.wx.enums;

/**
 * @author 褚超亮
 * @date 2020/4/7 20:23
 */
public enum EnumReply {
    /**
     * 评论正常状态
     */
    REPLY_NORMAL(0),

    /**
     * 评论删除状态
     */
    REPLY_DELETE_STATUS(1),

    /**
     * 评论展示数量
     */
    REPLY_SHOW(3);

    public Integer value;

    EnumReply(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
