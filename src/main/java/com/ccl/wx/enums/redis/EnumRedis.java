package com.ccl.wx.enums.redis;

/**
 * @author 褚超亮
 * @date 2020/3/2 15:16
 */
public enum EnumRedis {

    /**
     * 用户日志浏览量 前缀
     */
    DIARY_BROWSE("browse::diary"),

    /**
     * 为了和日记浏览的人数区分，设置这个日记浏览量前缀
     */
    NUMBER("sum"),

    /**
     * redis存储的点赞前缀
     */
    LIKE_PREFIX("like::"),

    /**
     * redis存储的点赞总数前缀
     */
    LIKE_SUM_PREFIX("account::"),

    /**
     * redis连接符号
     */
    REDIS_JOINT("::"),

    /**
     * 高频按钮 high::button::(userId)
     */
    HIGH_FREQUENCY_BUTTON("high::button"),

    /**
     * 低频按钮 low::button::(userId)
     */
    LOW_FREQUENCY_BUTTON("low::button"),

    /**
     * 加入圈子前缀 circle::join::(circleId)::(userId)
     */
    CIRCLE_JOIN_PREFIX("circle::join::"),

    /**
     * 退出圈子前缀 circle::exit::(circleId)::(userId)
     */
    CIRCLE_EXIT_PREFIX("circle::exit::");

    private final String value;

    EnumRedis(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
