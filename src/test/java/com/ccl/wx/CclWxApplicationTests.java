package com.ccl.wx;

import com.alibaba.fastjson.JSON;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @SneakyThrows
    @Test
    public void test() {
        redisTemplate.opsForValue().set("aa", "a", 10, TimeUnit.SECONDS);
    }

    /**
     * 手动同步点赞数据
     */
    @Test
    public void test1() {
        List<Map> userVitalityRanking = joinCircleMapper.getUserVitalityRanking(9L, 0, 10);
        List<Map> userSignInRanking = joinCircleMapper.getUserSignInRanking(9L, 0, 10);
        //String rownum = (String) userVitalityRanking.get(0).get("rownum");
        System.out.println(userVitalityRanking.get(0).get("rownum"));
        //System.out.println(userVitalityRanking.get(0));
        System.out.println(userSignInRanking.toString());
        //System.out.println(userVitalityRanking.toString());
    }

    @Test
    public void test2() {
        List<Map> userSignInRankingInfo = joinCircleService.getUserSignInRankingInfo(9L, "o1x2q5czO_xCH9eemeEfL41_gvMk", 0, 5);
        if (userSignInRankingInfo.size() == 0) {
            System.out.println("用户不在名词之内");
        } else {
            System.out.println("用户连续打卡信息：" + userSignInRankingInfo);
        }
    }

    @Test
    public void test3() {
        boolean idIsNull = userDiaryService.judgeThemeIdIsNull("o1x2q5czO_xCH9eemeEfL41_gvMk", 5L, "2020-03-10", 2);
        System.out.println(idIsNull);
    }

    @Test
    public void test4() {
        System.out.println();
        Map hashMap = new HashMap();
        hashMap.put("1", "1");
        hashMap.put("2", "2");
        hashMap.put("3", "3");
        hashMap.put("4", "5");
        //System.out.println(hashMap.toString());
        List<String> strings = joinCircleMapper.selectUseridGetVitalitySort(9L, 0, 0, 10);
        JSON.toJSONString(strings.toString());
        //System.out.println(JSON.toJSONString(hashMap));
    }
}
