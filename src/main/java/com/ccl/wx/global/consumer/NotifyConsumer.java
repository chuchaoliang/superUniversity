package com.ccl.wx.global.consumer;

import com.ccl.wx.config.properties.RabbitMQData;
import com.ccl.wx.service.UserNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author 褚超亮
 * @date 2020/5/23 21:53
 */
@Component
@Slf4j
public class NotifyConsumer {
    @Resource
    private UserNotifyService userNotifyService;

    /**
     * 监听点赞消息
     *
     * @param msg
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQData.DIARY_LIKE)
    public void like(String msg) throws IOException {
        userNotifyService.userMessageDispose(msg);
    }

    /**
     * 监听评论消息
     *
     * @param msg
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQData.DIARY_COMMON_COMMENT)
    public void commonComment(String msg) throws IOException {
        userNotifyService.userMessageDispose(msg);
    }

    /**
     * 监听回复消息
     *
     * @param msg
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQData.DIARY_REPLY)
    public void reply(String msg) throws IOException {
        userNotifyService.userMessageDispose(msg);
    }

    /**
     * 监听点评消息
     *
     * @param msg
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQData.DIARY_COMMENT)
    public void comment(String msg) throws IOException {
        userNotifyService.userMessageDispose(msg);
    }

    /**
     * 监听加入圈子
     *
     * @param msg
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQData.CIRCLE_JOIN)
    public void join(String msg) throws IOException {
        userNotifyService.userMessageDispose(msg);
    }

    /**
     * 监听申请圈子
     *
     * @param msg
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQData.CIRCLE_APPLY)
    public void apply(String msg) throws IOException {
        userNotifyService.userMessageDispose(msg);
    }

    /**
     * 监听拒绝用户加入圈子
     *
     * @param msg
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQData.CIRCLE_REFUSE)
    public void refuse(String msg) throws IOException {
        userNotifyService.userMessageDispose(msg);
    }

    /**
     * 监听同意用户加入圈子
     *
     * @param msg
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQData.CIRCLE_AGREE)
    public void agree(String msg) throws IOException {
        userNotifyService.userMessageDispose(msg);
    }

    /**
     * 监听淘汰圈子用户
     *
     * @param msg
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQData.CIRCLE_OUT)
    public void out(String msg) throws IOException {
        userNotifyService.userMessageDispose(msg);
    }

    /**
     * 监听退出圈子用户
     *
     * @param msg
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQData.CIRCLE_EXIT)
    public void exit(String msg) throws IOException {
        userNotifyService.userMessageDispose(msg);
    }

    /**
     * 监听系统、用户通知
     *
     * @param msg
     */
    @RabbitListener(queues = RabbitMQData.COMMON_NOTICE)
    public void notice(String msg) throws IOException {
        userNotifyService.userMessageDispose(msg);
    }

    /**
     * 监听系统、用户通知
     *
     * @param msg
     */
    @RabbitListener(queues = RabbitMQData.USER_CHAT)
    public void chat(String msg) {
        log.info("chat壹号");
        userNotifyService.userChatMessageDispose(msg);
    }

    /**
     * 监听系统、用户通知
     *
     * @param msg
     */
    @RabbitListener(queues = RabbitMQData.USER_CHAT)
    public void chat1(String msg) {
        log.info("chat二号");
        userNotifyService.userChatMessageDispose(msg);
    }

    /**
     * 监听系统、用户通知
     *
     * @param msg
     */
    @RabbitListener(queues = RabbitMQData.USER_CHAT)
    public void chat2(String msg) {
        log.info("chat三号");
        userNotifyService.userChatMessageDispose(msg);
    }
}
