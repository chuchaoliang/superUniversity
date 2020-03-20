package com.ccl.wx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 定时任务开启、缓存开启、事务开启、扫描mapper
 * SpringBoot启动类
 *
 * @author CCL
 */
@EnableScheduling
@EnableCaching(proxyTargetClass = true)
@EnableTransactionManagement
@MapperScan("com.ccl.wx.mapper")
@SpringBootApplication
public class CclWxApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CclWxApplication.class, args);
    }
}

