package com.ccl.wx.controller;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.mapper.SystemScrollMapper;
import com.ccl.wx.entity.SystemScroll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 褚超亮
 * @date 2020/2/14 11:30
 */
@RestController
@RequestMapping("/wx")
public class SystemOperationController {

    @Autowired
    private SystemScrollMapper systemScrollMapper;

    public static final Integer SYSTEM_SCROLL_KEY = 1;

    @GetMapping("/getscroll")
    public String getSystemScrollContent() {
        SystemScroll systemScroll = systemScrollMapper.selectByPrimaryKey(SYSTEM_SCROLL_KEY);
        return JSON.toJSONString(systemScroll.getScrollContent());
    }
}
