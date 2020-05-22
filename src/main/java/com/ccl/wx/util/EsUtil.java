package com.ccl.wx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccl.wx.enums.common.EnumResultStatus;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * elasticsearch 工具类
 *
 * @author 褚超亮
 * @date 2020/4/26 10:31
 */
public class EsUtil {
    /**
     * es 高亮搜索封装 单参数
     *
     * @param hit
     * @param param 字段名称
     * @return
     */
    public static String esHighlightSearch(SearchHit hit, String param) {
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        HighlightField highlightField = highlightFields.get(param);
        if (highlightField != null) {
            StringBuilder result = new StringBuilder();
            Text[] fragments = highlightField.getFragments();
            for (Text fragment : fragments) {
                result.append(fragment);
            }
            return result.toString();
        }
        return null;
    }

    /**
     * es 高亮搜索封装 多参数列表
     *
     * @param hit
     * @param paramList 字段名称列表
     * @return
     */
    public static Map<String, String> esListHighlightSearch(SearchHit hit, List<String> paramList) {
        Map<String, String> hashMap = new HashMap<>(16);
        for (String param : paramList) {
            hashMap.put(param, esHighlightSearch(hit, param));
        }
        return hashMap;
    }

    /**
     * 判断索引是否存在
     *
     * @param index 索引
     * @return
     */
    public static boolean indexIsExists(RestHighLevelClient client, String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 创建索引
     *
     * @param client
     * @param index  索引名称
     * @return 是否创建成功(true成功false失败)
     * @throws IOException
     */
    public static boolean createIndex(RestHighLevelClient client, String index) throws IOException {
        // 1. 创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(index);
        // 2. 客户端执行请求 IndicesClient，请求后获得响应
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @param client
     * @param index  索引名称
     * @return 是否删除成功 (true成功false失败)
     * @throws IOException
     */
    public static boolean deleteIndex(RestHighLevelClient client, String index) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        return delete.isAcknowledged();
    }

    /**
     * 添加文档
     *
     * @param client
     * @param index  索引名称
     * @param data   数据
     * @param id     对应id
     */
    public static void addDocument(RestHighLevelClient client, String index, Object data, String id) throws IOException {
        IndexRequest indexRequest = new IndexRequest(index).id(id).timeout(TimeValue.timeValueMinutes(1L))
                .source(JSON.toJSONString(data), XContentType.JSON);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 批量添加文档
     *
     * @param client   高级客户端
     * @param index    索引
     * @param dataList 数据列表
     * @param id       文档id （数据列表中所在代表的字段名称）
     * @return 是否操作成功 (true是 false否)
     */
    public static boolean addDocuments(RestHighLevelClient client, String index, List dataList, String id) throws IOException {
        if (!indexIsExists(client, index)) {
            // 索引不存在 创建索引
            if (!createIndex(client, index)) {
                // 创建失败
                return false;
            }
        }
        BulkRequest bulkRequest = new BulkRequest(index);
        for (Object data : dataList) {
            String text = JSON.toJSONString(data);
            JSONObject jsonObject = JSON.parseObject(text);
            bulkRequest.add(new IndexRequest().id(String.valueOf(jsonObject.get(id))).source(text, XContentType.JSON));
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    /**
     * 判断文档是否存在
     *
     * @param client
     * @param index  索引名称
     * @param id     对应id
     * @return 存在true false不存在
     * @throws IOException
     */
    public static boolean documentIsExists(RestHighLevelClient client, String index, String id) throws IOException {
        GetRequest getRequest = new GetRequest(index, id);
        return client.exists(getRequest, RequestOptions.DEFAULT);
    }

    /**
     * 获取文档内容
     *
     * @param client
     * @param index  索引名称
     * @param id     对应id
     * @return 存在返回文档内容json字符串 不存在 返回null
     */
    public static String getDocument(RestHighLevelClient client, String index, String id) throws IOException {
        if (documentIsExists(client, index, id)) {
            GetRequest getRequest = new GetRequest(index, id);
            GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
            return documentFields.getSourceAsString();
        }
        return null;
    }

    /**
     * 更新文档信息
     *
     * @param client 高级客户端
     * @param data   数据
     * @param index  索引名称
     * @param id     文档id
     * @return
     */
    public static String updateDocument(RestHighLevelClient client, Object data, String index, String id) throws IOException {
        if (documentIsExists(client, index, id)) {
            UpdateRequest updateRequest = new UpdateRequest(index, id).timeout(TimeValue.timeValueMinutes(1));
            updateRequest.timeout("1s");
            updateRequest.doc(JSON.toJSONString(data), XContentType.JSON);
            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
            return updateResponse.getResult().getLowercase();
        }
        return EnumResultStatus.FAIL.getValue();
    }

    /**
     * 普通搜索 根据searchRequest条件
     *
     * @param client
     * @param searchRequest 条件
     * @return
     */
    public static SearchResponse search(RestHighLevelClient client, SearchRequest searchRequest) throws IOException {
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }
}
