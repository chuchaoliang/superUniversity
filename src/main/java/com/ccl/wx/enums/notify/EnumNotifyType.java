package com.ccl.wx.enums.notify;

/**
 * 通知的所有动作类型
 *
 * @author 褚超亮
 * @date 2020/5/22 18:37
 */
public enum EnumNotifyType {
    // 系统通知
    SYSTEM_NOTICE(0, 0),
    // 点赞
    DIARY_LIKE(1, 3),
    // 评论
    DIARY_COMMON_COMMENT(2, 1),
    // 点评
    DIARY_COMMENT(3, 2),
    // 回复
    DIARY_REPLY(4, 2),
    //申请加入圈子
    CIRCLE_APPLY(5, 0),
    // 拒绝加入圈子
    CIRCLE_REFUSE(6, 0),
    // 同意加入圈子
    CIRCLE_AGREE(7, 0),
    // 淘汰
    CIRCLE_OUT(8, 0),
    // 加入圈子
    CIRCLE_JOIN(9, 0),
    // 退出圈子
    CIRCLE_EXIT(10, 0),
    // 关注
    USER_ATTENTION(11, 0),
    // 取消关注
    USER_CALL_ATTENTION(12, 0),
    // 私信
    USER_CHAT(13, 0),
    // 公告
    COMMON_NOTICE(14, 0);

    private int value;

    /**
     * 消息所在位置
     */
    private int location;

    EnumNotifyType(int value, int location) {
        this.value = value;
        this.location = location;
    }

    public int getValue() {
        return value;
    }

    public int getLocation() {
        return location;
    }
}
