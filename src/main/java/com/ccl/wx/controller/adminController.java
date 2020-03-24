package com.ccl.wx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.mapper.JoinCircleMapper;
import com.ccl.wx.service.JoinCircleService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author 褚超亮
 * @date 2020/2/18 20:22
 */
@RestController
@Validated
public class adminController {

    @Autowired
    private JoinCircleMapper joinCircleMapper;

    @Autowired
    private JoinCircleService joinCircleService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/test")
    public String test(@ParamCheck @RequestParam(value = "name", required = false) String name,
                       @Max(value = 10, message = "年龄不能大于10") @RequestParam(value = "age") Integer age) {
        System.out.println(name);
        System.out.println(age);
        return name;
    }

    @PostMapping("/test1")
    public String test1(@Valid @RequestBody UserInfo userInfo) {
        return JSONObject.toJSONString(userInfo);
    }

    @PostMapping("/test2")
    public String test2(@Valid @RequestBody UserInfo userInfo) {
        System.out.println(userInfo);
        return "OK";
    }

    @GetMapping("/test3")
    public String test3() {
        List<Map> userVitalityRanking = joinCircleMapper.getUserVitalityRanking(9L, 0, 10);
        List<Map> userSignInRanking = joinCircleMapper.getUserSignInRanking(9L, 0, 10);
        List<String> str = new ArrayList<>();
        str.add("1");
        str.add("2");
        str.add("3");
        str.add("4");
        ArrayList<List> str1 = new ArrayList<>();
        str1.add(userVitalityRanking);
        str1.add(str);
        return JSON.toJSONString(str1);
    }

    @GetMapping("/test4")
    public String test4() {
        List<Map> userInfo = joinCircleService.getUserVitalityRankingInfo(9L, "o1x2q5czO_xCH9eemeEfL41_gvMk", 0, 5);
        List<Map> userInfo1 = joinCircleService.getUserSignInRankingInfo(9L, "o1x2q5czO_xCH9eemeEfL41_gvMk", 0, 200);
        return JSON.toJSONString(userInfo1);
    }

    @GetMapping("/test5")
    public String test5() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        return "ok";
    }
}
