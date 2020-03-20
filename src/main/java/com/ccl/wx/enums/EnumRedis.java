package com.ccl.wx.enums;

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
     * redis连接符号
     */
    REDIS_JOINT("::");

    private String value;

    EnumRedis(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
