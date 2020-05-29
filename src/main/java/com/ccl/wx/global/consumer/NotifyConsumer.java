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
     * @param msg
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQData.DIARY_COMMENT)
    public void comment(String msg) throws IOException {
        userNotifyService.userMessageDispose(msg);
    }
}
