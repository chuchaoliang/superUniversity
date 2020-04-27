package com.ccl.wx.config;

import com.ccl.wx.config.properties.ElasticSearchProperties;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author 褚超亮
 * @date 2020/4/24 10:17
 */
@Configuration
public class ElasticSearchClientConfig {

    @Resource
    private ElasticSearchProperties elasticSearchProperties;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(elasticSearchProperties.getHostname(), elasticSearchProperties.getPort(), elasticSearchProperties.getScheme()))
        );
    }
}
