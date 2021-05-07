package com.cs.es.common;

import com.cs.common.bean.PagedResult;
import com.cs.es.entity.KeywordMapping;
import com.cs.es.mapper.KeywordMappingMapper;
import com.cs.es.mapper.KeywordMatchMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @ClassName: EsRestService
 * @Author: CS
 * @Date: 2019/10/8 14:15
 * @Description:
 */
@Slf4j
@Component
public class
EsRestService {


    @Autowired
    RestHighLevelClient client;

    @Autowired
    QueryItemsBuilder queryItemsBuilder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KeywordMappingMapper keywordMappingMapper;

    @Autowired
    KeywordMatchMapper keywordMatchMapper;

    private static final int TIMEOUT = 15;
    private static final int SHOW_SIZE = 1000;


    /**
     * 检查连接
     *
     * @return
     */
    public String testClient() {
        try {
            return "es连接成功：" + client.ping(RequestOptions.DEFAULT);
        } catch (IOException e) {
            return "es连接失败：" + e.getMessage();
        }
    }

    /**
     * 检查索引是否存在
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean checkoutIndexExists(String indexName) {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        request.local(false);
        request.humanReadable(true);
        try {
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("未检查到索引");
        }
        return false;
    }


    /**
     * 创建或更新
     *
     * @param index
     * @param documentId
     * @param dataJson
     * @return
     */
    public void createOrUpdateDocument(EsIndices index, String documentId, String dataJson) {
        IndexRequest request = new IndexRequest(index.getIndexName(), index.getIndexType(), documentId);
        request.source(dataJson, XContentType.JSON);
        try {
            IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
            log.info("结果为：{}", indexResponse.getResult().getLowercase());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    /**
     * 批量创建或更新
     *
     * @param index
     * @param jsons
     * @return
     */
    public void bulkCreateDocument(EsIndices index, List<String> jsons) {
        BulkRequest bulkRequest = new BulkRequest();
        for (String json : jsons) {
            IndexRequest request = new IndexRequest(index.getIndexName(), index.getIndexType());
            request.source(json, XContentType.JSON);
            bulkRequest.add(request);
        }
        try {
            BulkResponse indexResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            BulkItemResponse[] items = indexResponse.getItems();
            for (BulkItemResponse bulkItemResponse : items) {
                if (bulkItemResponse.isFailed()) {
                    log.info("结果为：{}", bulkItemResponse.getFailureMessage());
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    /**
     * 分页查询
     *
     * @param index
     * @param condition
     * @param pageIndex
     * @param pageSize
     * @param sortColumn
     * @param desc
     * @return
     */
    public <T> PagedResult<T> pagedSearch(EsIndices index, String keyword, List<Map<String, Object>> condition, TypeReference<T> typeReference, int pageIndex, int pageSize, String sortColumn, boolean desc) {
        KeywordMapping s = new KeywordMapping();
        s.setKeyword(keyword);
        List<KeywordMapping> keywordMapping = keywordMappingMapper.select(s);
        List<String> bm = keywordMatchMapper.selectBmByKeywordAndOrderBy(keyword);
        String gjz = keywordMapping.stream().flatMap(e -> e.getMapping().stream()).collect(Collectors.joining(","));
        gjz = keyword + gjz;
        SearchRequest request = new SearchRequest(index.getIndexName());
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 动态调节权重boost
        AtomicInteger boost = new AtomicInteger(11);
        bm.stream().limit(5).forEach(e -> boolQueryBuilder.should(QueryBuilders.matchQuery("bm", e).boost(boost.getAndAdd(-2))));
        boolQueryBuilder.should(QueryBuilders.matchQuery("mc", keyword).boost(2.0f));
        boolQueryBuilder.should(QueryBuilders.matchQuery("gjz", gjz).boost(1.0f));
        builder.query(boolQueryBuilder);
        builder.from((pageIndex > 0 ? pageIndex - 1 : 0) * pageSize);
        builder.size(pageSize);
        builder.timeout(new TimeValue(TIMEOUT, TimeUnit.SECONDS));
        log.info(builder.toString());
        request.source(builder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            List<T> data = new ArrayList<>();
            hits.forEach(hit -> {
                try {
                    data.add(objectMapper.readValue(hit.getSourceAsString(), typeReference));
                } catch (IOException e) {
                    log.error("序列化出错，原因是：{}", e.getCause().getMessage());
                }
            });
            return new PagedResult<T>().setRows(data).setTotal(hits.getTotalHits());
        } catch (Exception e) {
            log.error("查询索引出错，原因是：{}", e.getCause().getMessage());
            return null;
        }
    }


    /**
     * 项聚合
     *
     * @return
     */
    private void addTermsAggregationBuilder(SearchSourceBuilder source, String field, Integer size, Integer minCount) {
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(field)
                .field(field)
                .minDocCount(minCount)
                .order(Lists.newArrayList(BucketOrder.key(true), BucketOrder.count(true)));
        source.aggregation(termsAggregationBuilder);
    }

    /**
     * 项聚合
     *
     * @return
     */
    private void addTermsAggregationBuilder(TermsAggregationBuilder aggregationBuilder, TermsAggregationBuilder subAggregationBuilder) {
        aggregationBuilder.subAggregation(subAggregationBuilder);
    }


    /**
     * 日期格式直方图
     *
     * @param source
     */
    private void addDateHistogramAggregationBuilder(SearchSourceBuilder source) {
        DateHistogramAggregationBuilder dateHistogramAggregationBuilder = AggregationBuilders.dateHistogram("dateHistogram")
                .field("issuetime")
                .format("yyyy-MM-dd")
                .dateHistogramInterval(DateHistogramInterval.DAY)
                .order(BucketOrder.key(true))
                .extendedBounds(new ExtendedBounds("2019-09-01", "2019-09-24"))
                .minDocCount(0);
        source.aggregation(dateHistogramAggregationBuilder);
    }

}
