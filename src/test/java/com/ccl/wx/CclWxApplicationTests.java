package com.ccl.wx;

import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.UserChatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Set;

@Slf4j
//@RunWith(SpringRunner.class)
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
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("1", "2");
        hashMap.put("2", "2");
        hashMap.put("3", "2");
        hashMap.put("4", "2");
        hashMap.put("5", "2");
        Set<String> strings = hashMap.keySet();
        System.out.println(strings);
    }
}
