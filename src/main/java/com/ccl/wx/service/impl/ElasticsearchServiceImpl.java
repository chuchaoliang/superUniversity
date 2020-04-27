package com.ccl.wx.service.impl;

import com.ccl.wx.service.ElasticsearchService;
import com.ccl.wx.util.EsUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 褚超亮
 * @date 2020/4/26 9:55
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Resource
    private RestHighLevelClient client;

    @Override
    public String esHighlightSearch(SearchHit hit, String param) {
        return EsUtil.esHighlightSearch(hit, param);
    }

    @Override
    public Map<String, String> esListHighlightSearch(SearchHit hit, List<String> paramList) {
        return EsUtil.esListHighlightSearch(hit, paramList);
    }

    @Override
    public boolean indexIsExists(String index) throws IOException {
        return EsUtil.indexIsExists(client, index);
    }

    @Override
    public boolean createIndex(String index) throws IOException {
        return EsUtil.createIndex(client, index);
    }

    @Override
    public boolean deleteIndex(String index) throws IOException {
        return EsUtil.deleteIndex(client, index);
    }

    @Override
    public void addDocument(String index, Object data, String id) throws IOException {
        EsUtil.addDocument(client, index, data, id);
    }

    @Override
    public boolean addDocuments(String index, List dataList, String id) throws IOException {
        return EsUtil.addDocuments(client, index, dataList, id);
    }

    @Override
    public boolean documentIsExists(String index, String id) throws IOException {
        return EsUtil.documentIsExists(client, index, id);
    }

    @Override
    public String getDocument(String index, String id) throws IOException {
        return EsUtil.getDocument(client, index, id);
    }

    @Override
    public String updateDocument(Object data, String index, String id) throws IOException {
        return EsUtil.updateDocument(client, data, index, id);
    }

    @Override
    public SearchResponse search(SearchRequest searchRequest) throws IOException {
        return EsUtil.search(client, searchRequest);
    }
}
