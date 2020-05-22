package com.ccl.wx.enums.notify;

/**
 * 通知的所有动作类型
 *
 * @author 褚超亮
 * @date 2020/5/22 18:37
 */
public enum EnumNotifyType {
    // 点赞
    DIARY_LIKE(0),
    // 评论
    DIARY_COMMON_COMMENT(1),
    // 点评
    DIARY_COMMENT(2),
    // 回复
    DIARY_REPLY(3),
    //申请加入圈子
    CIRCLE_APPLY(4),
    // 拒绝加入圈子
    CIRCLE_REFUSE(5),
    // 同意加入圈子
    CIRCLE_AGREE(6),
    // 淘汰
    CIRCLE_OUT(7),
    // 加入圈子
    CIRCLE_JOIN(8),
    // 退出圈子
    CIRCLE_EXIT(9),
    // 关注
    USER_ATTENTION(10),
    // 取消关注
    USER_CALL_ATTENTION(11),
    // 私信
    USER_CHAT(12),
    // 公告
    SYSTEM_NOTICE(13);

    private int value;

    EnumNotifyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
