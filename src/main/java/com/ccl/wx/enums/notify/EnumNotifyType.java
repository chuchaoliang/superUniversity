package com.ccl.wx.enums.notify;

import com.ccl.wx.common.notify.IUserNotify;
import com.ccl.wx.config.properties.RabbitMQData;

/**
 * 通知的所有动作类型
 *
 * @author 褚超亮
 * @date 2020/5/22 18:37
 */
public enum EnumNotifyType implements IUserNotify {
    // 系统用户通知
    SYSTEM_NOTICE(0, 0, 2, RabbitMQData.SYSTEM_NOTICE),
    // 点赞
    DIARY_LIKE(1, 3, 0, RabbitMQData.DIARY_LIKE),
    // 评论
    DIARY_COMMON_COMMENT(2, 1, 0, RabbitMQData.DIARY_COMMON_COMMENT),
    // 点评
    DIARY_COMMENT(3, 2, 0, RabbitMQData.DIARY_COMMENT),
    // 回复
    DIARY_REPLY(4, 2, 5, RabbitMQData.DIARY_REPLY),
    //申请加入圈子
    CIRCLE_APPLY(5, 0, 4, RabbitMQData.CIRCLE_APPLY),
    // 拒绝加入圈子
    CIRCLE_REFUSE(6, 0, 4, RabbitMQData.CIRCLE_REFUSE),
    // 同意加入圈子
    CIRCLE_AGREE(7, 0, 4, RabbitMQData.CIRCLE_AGREE),
    // 淘汰
    CIRCLE_OUT(8, 0, 4, RabbitMQData.CIRCLE_OUT),
    // 加入圈子
    CIRCLE_JOIN(9, 0, 4, RabbitMQData.CIRCLE_JOIN),
    // 退出圈子
    CIRCLE_EXIT(10, 0, 4, RabbitMQData.CIRCLE_EXIT),
    // 关注
    USER_ATTENTION(11, 0, 1, RabbitMQData.USER_ATTENTION),
    // 取消关注
    USER_CALL_ATTENTION(12, 0, 1, RabbitMQData.USER_CALL_ATTENTION),
    // 私信
    USER_CHAT(13, 0, 3, RabbitMQData.USER_CHAT),
    // 普通用户公告
    COMMON_NOTICE(14, 0, 2, RabbitMQData.COMMON_NOTICE);

    /**
     * 动作类型
     * 0.系统
     * 1.点赞
     * 2.评论
     * 3.点评
     * 4.回复
     * 5.申请
     * 6.拒绝
     * 7.同意
     * 8.淘汰
     * 9.加入
     * 10.退出
     * 11.关注
     * 12.取消关注
     * 13.私信
     * 14.公告（通知）
     */
    private final int notifyType;

    /**
     * 消息所在位置
     * 0.通知
     * 1.评论
     * 2.点评
     * 3.点赞
     */
    private final int notifyLocation;

    /**
     * 资源类型
     * 0日志
     * 1用户
     * 2通知
     * 3私信
     * 4圈子
     * 5评论
     */
    private final int resourceType;

    /**
     * 队列的键和名字（queue）
     */
    private final String queue;

    /**
     * 获取交换机的名称
     */
    public static final String EXCHANGE_NAME = RabbitMQData.NOTIFY_EXCHANGE_NAME;

    EnumNotifyType(int notifyType, int notifyLocation, int resourceType, String queue) {
        this.notifyType = notifyType;
        this.notifyLocation = notifyLocation;
        this.resourceType = resourceType;
        this.queue = queue;
    }

    @Override
    public Integer getNotifyType() {
        return notifyType;
    }

    @Override
    public Integer getNotifyLocation() {
        return notifyLocation;
    }

    @Override
    public Integer getResourceType() {
        return resourceType;
    }

    @Override
    public String getQueue() {
        return queue;
    }
}
