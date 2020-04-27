package com.ccl.wx.controller;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.common.api.Result;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.util.EsUtil;
import com.ccl.wx.util.ResponseMsgUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * @author 褚超亮
 * @date 2020/2/18 20:22
 */
@RestController
@Validated
public class adminController {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/test")
    public Result<String> test() throws IOException {
        // 构造查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("country", "中国"))
                .must(QueryBuilders.matchQuery("province", "河北"))
                .filter(QueryBuilders.rangeQuery("userId").gte(0).lte(25));
        SearchRequest searchRequest =
                new SearchRequest().source(
                        new SearchSourceBuilder()
                                .query(boolQueryBuilder)
                                .highlighter(
                                        new HighlightBuilder().field("province").requireFieldMatch(false).preTags("<span style='color:#E4393C'>").postTags("</span>")
                                )
                                .from(0).size(5));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        long value = searchResponse.getHits().getTotalHits().value;
        System.out.println("value" + value);
        for (SearchHit hit : searchResponse.getHits()) {
            // 替换高亮展示
            String sourceAsString = hit.getSourceAsString();
            UserInfo userInfo = JSON.parseObject(sourceAsString, UserInfo.class);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField province = highlightFields.get("province");
            if (province != null) {
                Text[] fragments = province.getFragments();
                StringBuilder newProvince = new StringBuilder();
                for (Text fragment : fragments) {
                    newProvince.append(fragment);
                }
                userInfo.setProvince(newProvince.toString());
            }
            userInfos.add(userInfo);
        }
        return ResponseMsgUtil.success(JSON.toJSONStringWithDateFormat(userInfos, DatePattern.NORM_DATETIME_MINUTE_PATTERN, SerializerFeature.WriteDateUseDateFormat));
    }

    @GetMapping("/test1")
    public Result<String> test1() throws IOException {
        // 构造查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("country", "中国"))
                .should(QueryBuilders.matchQuery("province", "河"));
        SearchRequest searchRequest = new SearchRequest("user");
        searchRequest.source(new SearchSourceBuilder()
                .sort("createtime", SortOrder.ASC)
                .sort("_id", SortOrder.ASC)
                .query(boolQueryBuilder).highlighter(new HighlightBuilder().field("*").preTags("<em>").postTags("</em>"))
        );
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits) {
            Map<String, String> stringStringMap = EsUtil.esListHighlightSearch(hit, Arrays.asList("name", "desc"));
            Object[] sortValues = hit.getSortValues();
            for (Object sortValue : sortValues) {
                System.out.println(sortValue);
            }
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }
}
