package com.ccl.wx.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.ccl.wx.mapper.JoinCircleMapper;
import com.ccl.wx.service.JoinCircleService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

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
    public String test(@RequestParam(value = "date", required = false) Date date) {
        String format = DateUtil.format(date, DatePattern.NORM_DATETIME_MINUTE_PATTERN);
        return format;
    }
}
