package com.ccl.wx;

import com.ccl.wx.config.properties.DefaultProperties;
import com.ccl.wx.config.properties.ElasticSearchProperties;
import com.ccl.wx.config.properties.FileUploadProperties;
import com.ccl.wx.config.properties.FtpProperties;
import com.ccl.wx.mapper.*;
import com.ccl.wx.service.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ElasticSearchProperties elasticSearchProperties;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private CircleInfoService circleInfoService;

    @Resource
    private ElasticsearchService elasticsearchService;

    @Resource
    private AmqpAdmin amqpAdmin;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Test
    public void test() throws Exception {
    }
}
