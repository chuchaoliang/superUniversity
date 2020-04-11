package com.ccl.wx;

import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.mapper.*;
import com.ccl.wx.properties.DefaultProperties;
import com.ccl.wx.properties.FileUploadProperties;
import com.ccl.wx.properties.FtpProperties;
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

    @Autowired
    private UserDiaryMapper userDiaryMapper;

    @Autowired
    private FtpProperties ftpProperties;

    @Autowired
    private FileUploadProperties fileUploadProperties;

    @SneakyThrows
    @Test
    public void test1() {
        CircleInfo circleInfo = new CircleInfo();
        circleInfo.setCircleMember(0).setThemeSum(0).setDiarySum(0);
        int i = circleInfoMapper.updateCircleData(circleInfo, 17L, 1);
        System.out.println(i);
    }

    @SneakyThrows
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Test
    public void test() throws Exception {
    }
}
