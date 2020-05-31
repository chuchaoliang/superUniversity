package com.ccl.wx.config.rabbitmq;

import com.ccl.wx.config.properties.RabbitMQData;
import com.ccl.wx.enums.notify.EnumNotifyType;
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
        return new Queue(EnumNotifyType.DIARY_LIKE.getQueue(), true);
    }

    /**
     * 添加评论队列
     *
     * @return
     */
    @Bean
    public Queue diaryCommonComment() {
        return new Queue(EnumNotifyType.DIARY_COMMON_COMMENT.getQueue(), true);
    }

    /**
     * 添加回复队列
     *
     * @return
     */
    @Bean
    public Queue diaryReply() {
        return new Queue(EnumNotifyType.DIARY_REPLY.getQueue(), true);
    }

    /**
     * 添加点评队列
     *
     * @return
     */
    @Bean
    public Queue diaryComment() {
        return new Queue(EnumNotifyType.DIARY_COMMENT.getQueue(), true);
    }

    /**
     * 添加加入圈子队列
     *
     * @return
     */
    @Bean
    public Queue circleJoin() {
        return new Queue(EnumNotifyType.CIRCLE_JOIN.getQueue(), true);
    }

    /**
     * 添加申请加入队列
     *
     * @return
     */
    @Bean
    public Queue circleApply() {
        return new Queue(EnumNotifyType.CIRCLE_APPLY.getQueue(), true);
    }

    /**
     * 添加拒绝加入圈子队列
     *
     * @return
     */
    @Bean
    public Queue circleRefuse() {
        return new Queue(EnumNotifyType.CIRCLE_REFUSE.getQueue(), true);
    }

    /**
     * 添加同意加入圈子队列
     *
     * @return
     */
    @Bean
    public Queue circleAgree() {
        return new Queue(EnumNotifyType.CIRCLE_AGREE.getQueue(), true);
    }

    /**
     * 添加淘汰加入圈子队列
     *
     * @return
     */
    @Bean
    public Queue circleOut() {
        return new Queue(EnumNotifyType.CIRCLE_OUT.getQueue(), true);
    }

    /**
     * 添加退出圈子队列
     *
     * @return
     */
    @Bean
    public Queue circleExit() {
        return new Queue(EnumNotifyType.CIRCLE_EXIT.getQueue(), true);
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
        return BindingBuilder.bind(diaryLikeQueue()).to(commonNotifyDirectExchange()).with(EnumNotifyType.DIARY_LIKE.getQueue());
    }

    /**
     * 绑定评论交换机
     *
     * @return
     */
    @Bean
    public Binding bindingDirectOfCommonComment() {
        return BindingBuilder.bind(diaryCommonComment()).to(commonNotifyDirectExchange()).with(EnumNotifyType.DIARY_COMMON_COMMENT.getQueue());
    }

    /**
     * 绑定回复交换机
     *
     * @return
     */
    @Bean
    public Binding bindingDirectOfReply() {
        return BindingBuilder.bind(diaryReply()).to(commonNotifyDirectExchange()).with(EnumNotifyType.DIARY_REPLY.getQueue());
    }

    /**
     * 绑定点评交换机
     *
     * @return
     */
    @Bean
    public Binding bindingDirectOfComment() {
        return BindingBuilder.bind(diaryComment()).to(commonNotifyDirectExchange()).with(EnumNotifyType.DIARY_COMMENT.getQueue());
    }

    /**
     * 绑定加入圈子交换机
     *
     * @return
     */
    @Bean
    public Binding bindingDirectOfJoin() {
        return BindingBuilder.bind(circleJoin()).to(commonNotifyDirectExchange()).with(EnumNotifyType.CIRCLE_JOIN.getQueue());
    }

    /**
     * 绑定申请加入圈子交换机
     *
     * @return
     */
    @Bean
    public Binding bindingDirectOfApply() {
        return BindingBuilder.bind(circleApply()).to(commonNotifyDirectExchange()).with(EnumNotifyType.CIRCLE_APPLY.getQueue());
    }

    /**
     * 绑定拒绝用户加入圈子交换机
     *
     * @return
     */
    @Bean
    public Binding bindingDirectOfRefuse() {
        return BindingBuilder.bind(circleRefuse()).to(commonNotifyDirectExchange()).with(EnumNotifyType.CIRCLE_REFUSE.getQueue());
    }

    /**
     * 绑定同意用户加入圈子交换机
     *
     * @return
     */
    @Bean
    public Binding bindingDirectOfAgree() {
        return BindingBuilder.bind(circleAgree()).to(commonNotifyDirectExchange()).with(EnumNotifyType.CIRCLE_AGREE.getQueue());
    }

    /**
     * 绑定淘汰圈子用户交换机
     *
     * @return
     */
    @Bean
    public Binding bindingDirectOfOut() {
        return BindingBuilder.bind(circleOut()).to(commonNotifyDirectExchange()).with(EnumNotifyType.CIRCLE_OUT.getQueue());
    }

    /**
     * 绑定退出圈子用户交换机
     *
     * @return
     */
    @Bean
    public Binding bindingDirectOfExit() {
        return BindingBuilder.bind(circleExit()).to(commonNotifyDirectExchange()).with(EnumNotifyType.CIRCLE_EXIT.getQueue());
    }
}
