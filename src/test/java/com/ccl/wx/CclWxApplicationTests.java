package com.ccl.wx;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.config.properties.RabbitMQData;
import com.ccl.wx.entity.UserNotify;
import com.ccl.wx.enums.notify.EnumNotifyResourceType;
import com.ccl.wx.enums.notify.EnumNotifyType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CclWxApplicationTests {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Test
    public void test() throws Exception {
        // 保存数据到mysql通知表中
        UserNotify userNotify = new UserNotify();
        // 设置目标用户id
        userNotify.setTargetId("o1x2q5czO_xCH9eemeEfL41_gvMk");
        // 设置发送者用户id
        userNotify.setSenderId("o1x2q5czO_xCH9eemeEfL41_gvMk");
        // 设置资源类型
        userNotify.setResourceType((byte) EnumNotifyResourceType.DIARY.getValue());
        // 设置资源id
        userNotify.setResourceId(369);
        // 设置消息所在位置
        userNotify.setLocation((byte) EnumNotifyType.DIARY_LIKE.getLocation());

        rabbitTemplate.convertAndSend(RabbitMQData.COMMON_NOTIFY_EXCHANGE, RabbitMQData.LIKE, JSON.toJSONString(userNotify));
    }
}
