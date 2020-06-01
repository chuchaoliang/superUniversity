package com.ccl.wx;

import com.ccl.wx.entity.UserChat;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.UserChatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private JoinCircleService joinCircleService;

    @Resource
    private UserChatService userChatService;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Test
    public void test() throws Exception {
        UserChat userChat = new UserChat();
        userChat.setContent("ss");
        userChat.setTargetId("sss");
        userChat.setSendId("ssss");
        int i = userChatService.insertSelective(userChat);
        System.out.println("主键" + userChat.getId());
    }
}
