package com.ccl.wx;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.ccl.wx.config.properties.DefaultProperties;
import com.ccl.wx.config.properties.FileUploadProperties;
import com.ccl.wx.config.properties.FtpProperties;
import com.ccl.wx.mapper.*;
import com.ccl.wx.service.CircleScheduleService;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.TodayContentService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.CclDateUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
//@RunWith(SpringRunner.class)
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
        DateUtil.format(new Date(), DatePattern.NORM_TIME_PATTERN);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date parse = simpleDateFormat.parse("2020-04-10 10:20");
        String s = CclDateUtil.todayDate(new Date());
        System.out.println(s);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Test
    public void test() throws Exception {
    }
}
