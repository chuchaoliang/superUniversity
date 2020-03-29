package com.ccl.wx.config;

import com.ccl.wx.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Spring mvc 配置
 *
 * @author nicc
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 文件上传路径前缀
     */
    @Value("${file.upload.suffix.path}")
    public String uploadSuffixPath;
    /**
     * 本地磁盘目录
     */
    @Value("${file.upload.local.path}")
    public String uploadLocalPath;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/welcome").setViewName("welcome");
    }

    /**
     * @Title: addResourceHandlers
     * @Description: 映射本地磁盘为静态目录
     * @param: registry
     * @throws:
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        FileUtil.uploadSuffixPath = uploadSuffixPath;
        FileUtil.uploadLocalPath = uploadLocalPath;
        registry.addResourceHandler(uploadSuffixPath + "/**").
                addResourceLocations("file:" + File.separator + "home" + File.separator + "userfile" + File.separator);
    }

    /**
     * 跨域
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //项目中的所有接口都支持跨域
        registry.addMapping("/**")
                //所有地址都可以访问，也可以配置具体地址
                .allowedOrigins("*")
                //允许的请求方式
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                //是否支持跨域Cookie
                .allowCredentials(true)
                // 跨域允许时间
                .maxAge(3600);
    }
}
