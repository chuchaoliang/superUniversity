package com.ccl.wx.config.rabbitmq;

import com.ccl.wx.config.properties.RabbitMQData;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
 * Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
 * Queue:消息的载体,每个消息都会被投到一个或多个队列。
 * Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
 * Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
 * vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
 * Producer:消息生产者,就是投递消息的程序.
 * Consumer:消息消费者,就是接受消息的程序.
 * Channel:消息通道,在客户端的每个连接里,可建立多个channel.
 *
 * @author 褚超亮
 * @date 2020/3/22 10:06
 */
@Configuration
public class RabbitConfig {
    // ==============================声明队列======================================

    /**
     * 添加点赞队列
     *
     * @return
     */
    @Bean
    public Queue diaryLikeQueue() {
        return new Queue(RabbitMQData.DIARY_LIKE, true);
    }

    /**
     * 添加评论队列
     *
     * @return
     */
    @Bean
    public Queue diaryCommonComment() {
        return new Queue(RabbitMQData.DIARY_COMMON_COMMENT, true);
    }

    // ==============================声明交换机======================================

    /**
     * 消息通知Direct交换机
     *
     * @return
     */
    @Bean
    public DirectExchange commonNotifyDirectExchange() {
        return new DirectExchange(RabbitMQData.NOTIFY_EXCHANGE_NAME, true, false);
    }

    // ==============================绑定======================================

    /**
     * 将点赞交换机绑定
     *
     * @return
     */
    @Bean
    public Binding bindingDirectOfLike() {
        return BindingBuilder.bind(diaryLikeQueue()).to(commonNotifyDirectExchange()).with(RabbitMQData.DIARY_LIKE);
    }

    /**
     * 绑定评论交换机
     *
     * @return
     */
    @Bean
    public Binding bindingDirectOfCommonComment() {
        return BindingBuilder.bind(diaryCommonComment()).to(commonNotifyDirectExchange()).with(RabbitMQData.DIARY_COMMON_COMMENT);
    }
}
