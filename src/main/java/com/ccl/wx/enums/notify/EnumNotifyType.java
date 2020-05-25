package com.ccl.wx.enums.notify;

/**
 * 通知的所有动作类型
 *
 * @author 褚超亮
 * @date 2020/5/22 18:37
 */
public enum EnumNotifyType {
    // 系统通知
    SYSTEM_NOTICE(0),
    // 点赞
    DIARY_LIKE(1),
    // 评论
    DIARY_COMMON_COMMENT(2),
    // 点评
    DIARY_COMMENT(3),
    // 回复
    DIARY_REPLY(4),
    //申请加入圈子
    CIRCLE_APPLY(5),
    // 拒绝加入圈子
    CIRCLE_REFUSE(6),
    // 同意加入圈子
    CIRCLE_AGREE(7),
    // 淘汰
    CIRCLE_OUT(8),
    // 加入圈子
    CIRCLE_JOIN(9),
    // 退出圈子
    CIRCLE_EXIT(10),
    // 关注
    USER_ATTENTION(11),
    // 取消关注
    USER_CALL_ATTENTION(12),
    // 私信
    USER_CHAT(13),
    // 公告
    COMMON_NOTICE(14);

    private int value;

    EnumNotifyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
