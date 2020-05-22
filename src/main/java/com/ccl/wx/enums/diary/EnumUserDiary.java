package com.ccl.wx.enums.diary;

/**
 * 用户日志状态
 *
 * @author 褚超亮
 * @date 2020/2/21 21:38
 */
public enum EnumUserDiary {
    /**
     * 日志常规状态（所有人可见）
     */
    USER_DIARY_NORMAL(0),

    /**
     * 日志仅圈子内成员可见
     */
    USER_DIARY_PERMISSION(1),

    /**
     * 日志删除状态
     */
    USER_DIARY_DELETE(2),

    /**
     * 日记主题删除状态
     */
    USER_DIARY_THEME_DELETE(3),

    /**
     * 用户日记违规状态
     */
    USER_DIARY_VIOLATION(4);

    private int value;

    EnumUserDiary(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
