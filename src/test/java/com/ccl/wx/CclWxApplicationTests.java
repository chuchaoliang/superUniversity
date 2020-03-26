package com.ccl.wx;

import com.ccl.wx.mapper.*;
import com.ccl.wx.properties.DefaultProperties;
import com.ccl.wx.service.CircleScheduleService;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.TodayContentService;
import com.ccl.wx.service.UserDiaryService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CclWxApplicationTests {


    @Autowired
    private CircleInfoMapper circleInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CircleScheduleService circleScheduleService;

    @Autowired
    private UserDiaryService userDiaryService;

    @Autowired
    private TodayContentMapper todayContentMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TodayContentService todayContentService;

    @Autowired
    private JoinCircleMapper joinCircleMapper;

    @Autowired
    private DefaultProperties defaultProperties;

    @Autowired
    private JoinCircleService joinCircleService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test1() {
        todayContentService.deleteCircleThemeFormDatabase();
    }

    @SneakyThrows
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Test
    public void test() {
        new Thread(() -> {
            System.out.println("-----");
            circleInfoMapper.selectByCircleId(5L);
            circleInfoMapper.updateCircleMemberByCircleId(5L, 1);
        }, "a").start();
        new Thread(() -> {
            System.out.println("-----");
            circleInfoMapper.selectByCircleId(5L);
            circleInfoMapper.updateCircleMemberByCircleId(5L, 1);
        }, "a").start();
        new Thread(() -> {
            System.out.println("-----");
            circleInfoMapper.selectByCircleId(5L);
            circleInfoMapper.updateCircleMemberByCircleId(5L, 1);
        }, "a").start();
        new Thread(() -> {
            System.out.println("-----");
            circleInfoMapper.selectByCircleId(5L);
            circleInfoMapper.updateCircleMemberByCircleId(5L, 1);
        }, "a").start();
        new Thread(() -> {
            System.out.println("-----");
            circleInfoMapper.selectByCircleId(5L);
            circleInfoMapper.updateCircleMemberByCircleId(5L, 1);
        }, "a").start();
        new Thread(() -> {
            System.out.println("-----");
            circleInfoMapper.selectByCircleId(5L);
            circleInfoMapper.updateCircleMemberByCircleId(5L, 1);
        }, "a").start();
        new Thread(() -> {
            System.out.println("-----");
            circleInfoMapper.selectByCircleId(5L);
            circleInfoMapper.updateCircleMemberByCircleId(5L, 1);
        }, "a").start();
        new Thread(() -> {
            System.out.println("-----");
            circleInfoMapper.selectByCircleId(5L);
            circleInfoMapper.updateCircleMemberByCircleId(5L, 1);
        }, "a").start();
        new Thread(() -> {
            System.out.println("-----");
            circleInfoMapper.selectByCircleId(5L);
            circleInfoMapper.updateCircleMemberByCircleId(5L, 1);
        }, "a").start();
        new Thread(() -> {
            System.out.println("-----");
            circleInfoMapper.selectByCircleId(5L);
            circleInfoMapper.updateCircleMemberByCircleId(5L, 1);
        }, "a").start();
    }
}
