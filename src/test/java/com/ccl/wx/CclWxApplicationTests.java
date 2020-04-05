package com.ccl.wx;

import com.ccl.wx.mapper.*;
import com.ccl.wx.properties.DefaultProperties;
import com.ccl.wx.properties.FileUploadProperties;
import com.ccl.wx.properties.FtpProperties;
import com.ccl.wx.service.CircleScheduleService;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.TodayContentService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.CclUtil;
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

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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
        System.out.println(CclUtil.getFileUploadAddress("o1x2q5czO_xCH9eemeEfL41_gvMk"));
    }

    @SneakyThrows
    @Test
    public void test3() {
        //URL url = new URL("https://ccllove.club/user/o1x2q5czO_xCH9eemeEfL41_gvMk/20200326/image/20200326102101_191a5ed8b05f497b8d36b69567559544.png");
        URL url = new URL("https://www.baidu.com");
        System.out.println("协议:" + url.getProtocol());
        System.out.println("主机:" + url.getHost());
        System.out.println("端口:" + url.getPort());
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        byte[] image = new byte[10240];
        int len;
        while ((len = inputStream.read(image)) != -1) {
            System.out.println(new String(image, 0, len));
        }
        //InputStream stream = URLUtil.getStream(url);
        //MultipartFile file = new MockMultipartFile("test.png", stream);
        //System.out.println(file.getName());
    }

    @SneakyThrows
    @Test
    public void test2() {
        URL url = new URL("https://ccllove.club/user/o1x2q5czO_xCH9eemeEfL41_gvMk/20200326/image/20200326102101_191a5ed8b05f497b8d36b69567559544.png");
        //URLConnection urlConnection = url.openConnection();
        //InputStream inputStream = urlConnection.getInputStream();
        //System.out.println(inputStream);
        //byte[] b = new byte[2048];
        //inputStream.read(b);
        //inputStream.close();
        //System.out.println("内容" + new String(b));
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        int length = urlConnection.getContentLength();
        int i = length;
        int c;
        while ((c = inputStream.read()) != -1 && --i > 0) {
            System.out.print((char) c);
        }
        inputStream.close();
        //FileInputStream fileInputStream = new FileInputStream(file);
        //// MockMultipartFile(String name, @Nullable String originalFilename, @Nullable String contentType, InputStream contentStream)
        //// 其中originalFilename,String contentType 旧名字，类型  可为空
        //// ContentType.APPLICATION_OCTET_STREAM.toString() 需要使用HttpClient的包
        //MultipartFile multipartFile = new MockMultipartFile("copy" + file.getName(), file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        //IOUtils.copy();
        //System.out.println(multipartFile.getName());
    }

    @SneakyThrows
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Test
    public void test() throws Exception {
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
