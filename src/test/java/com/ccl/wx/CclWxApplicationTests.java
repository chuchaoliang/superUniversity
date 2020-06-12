package com.ccl.wx;

import com.ccl.wx.mapper.UserNotifyMapper;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.UserChatService;
import com.ccl.wx.service.UserNotifyService;
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
    @Resource
    private UserNotifyService userNotifyService;
    @Resource
    private UserNotifyMapper userNotifyMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Test
    public void test() throws Exception {
    }
}
