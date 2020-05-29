package com.ccl.wx;

import com.ccl.wx.enums.notify.EnumNotifyType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
//@RunWith(SpringRunner.class)
@SpringBootTest
public class CclWxApplicationTests {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Test
    public void test() throws Exception {
        System.out.println(EnumNotifyType.DIARY_LIKE.getQueue());
        System.out.println(EnumNotifyType.DIARY_LIKE.getNotifyLocation());
        System.out.println(EnumNotifyType.DIARY_LIKE.getNotifyType());
        System.out.println(EnumNotifyType.DIARY_LIKE.getResourceType());
    }
}
