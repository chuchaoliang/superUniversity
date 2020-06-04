package com.ccl.wx.config.properties;

/**
 * @author 褚超亮
 * @date 2020/5/23 21:20
 */
@SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
public class RabbitMQData {
    // ============================= queue ==============================
    // 系统通知
    public static final String SYSTEM_NOTICE = "wx.notify.system.notice";
    // 日志--点赞
    public static final String DIARY_LIKE = "wx.notify.diary.like";
    // 日志--评论
    public static final String DIARY_COMMON_COMMENT = "wx.notify.diary.general.comment";
    // 日志--点评
    public static final String DIARY_COMMENT = "wx.notify.diary.master.comment";
    // 日志--回复
    public static final String DIARY_REPLY = "wx.notify.diary.reply";
    // 圈子--同意
    public static final String CIRCLE_APPLY = "wx.notify.circle.apply";
    // 圈子--拒接
    public static final String CIRCLE_REFUSE = "wx.notify.circle.refuse";
    // 圈子--同意
    public static final String CIRCLE_AGREE = "wx.notify.circle.agree";
    // 圈子--淘汰
    public static final String CIRCLE_OUT = "wx.notify.circle.out";
    // 圈子--加入
    public static final String CIRCLE_JOIN = "wx.notify.circle.join";
    // 圈子--退出
    public static final String CIRCLE_EXIT = "wx.notify.circle.exit";
    // 用户--关注
    public static final String USER_ATTENTION = "wx.notify.user.attention";
    // 用户--取消关注
    public static final String USER_CALL_ATTENTION = "wx.notify.user.cancel.attention";
    // 用户--私信
    public static final String USER_CHAT = "wx.notify.user.chat";
    // 普通通知
    public static final String COMMON_NOTICE = "wx.notify.notice";

    // ============================= 交换机 =========================================
    // 交换机名称
    public static final String NOTIFY_EXCHANGE_NAME = "wx.notify";
}
