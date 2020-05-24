package com.ccl.wx.global.consumer;

import com.ccl.wx.config.properties.RabbitMQData;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 褚超亮
 * @date 2020/5/23 21:53
 */
@Component
public class NotifyConsumer {
    /**
     * 监听点赞消息
     */
    @RabbitListener(queues = RabbitMQData.LIKE)
    public void like(String msg) {
        System.out.println("======>" + msg);
    }
}
