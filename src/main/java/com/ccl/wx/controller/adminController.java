package com.ccl.wx.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 褚超亮
 * @date 2020/2/18 20:22
 */
@RestController
@Validated
public class adminController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
