package com.ccl.wx.service;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 褚超亮
 * @date 2020/4/26 9:54
 */
public interface ElasticsearchService {
    /**
     * es 高亮搜索封装 单参数
     *
     * @param hit
     * @param param 字段名称
     * @return
     */
    String esHighlightSearch(SearchHit hit, String param);

    /**
     * es 高亮搜索封装 多参数列表
     *
     * @param hit
     * @param paramList 字段名称列表
     * @return
     */
    Map<String, String> esListHighlightSearch(SearchHit hit, List<String> paramList);

    /**
     * 判断索引是否存在
     *
     * @param index 索引
     * @return
     * @throws IOException
     */
    boolean indexIsExists(String index) throws IOException;

    /**
     * 创建索引
     *
     * @param index 索引名称
     * @return 是否创建成功(true成功false失败)
     * @throws IOException
     */
    boolean createIndex(String index) throws IOException;

    /**
     * 删除索引
     *
     * @param index 索引名称
     * @return 是否删除成功 (true成功false失败)
     * @throws IOException
     */
    boolean deleteIndex(String index) throws IOException;

    /**
     * 添加文档
     *
     * @param index 索引名称
     * @param data  数据
     * @param id    对应id
     * @throws IOException
     */
    void addDocument(String index, Object data, String id) throws IOException;

    /**
     * 批量添加文档
     *
     * @param index    索引
     * @param dataList 数据列表
     * @param id       文档id （数据列表中所在代表的字段名称）
     * @return 是否操作成功 (true是 false否)
     * @throws IOException
     */
    boolean addDocuments(String index, List<Object> dataList, String id) throws IOException;

    /**
     * 判断文档是否存在
     *
     * @param index 索引名称
     * @param id    对应id
     * @return 存在true false不存在
     * @throws IOException
     */
    boolean documentIsExists(String index, String id) throws IOException;

    /**
     * 获取文档内容
     *
     * @param index 索引名称
     * @param id    对应id
     * @return 存在返回文档内容json字符串 不存在 返回""
     * @throws IOException
     */
    String getDocument(String index, String id) throws IOException;

    /**
     * 更新文档信息
     *
     * @param data  数据
     * @param index 索引名称
     * @param id    文档id
     * @return
     * @throws IOException
     */
    String updateDocument(Object data, String index, String id) throws IOException;

    /**
     * 普通搜索 根据searchRequest条件
     *
     * @param searchRequest 条件
     * @return
     * @throws IOException
     */
    SearchResponse search(SearchRequest searchRequest) throws IOException;
}
