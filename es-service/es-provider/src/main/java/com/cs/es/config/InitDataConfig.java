package com.cs.es.config;

import com.cs.es.common.EsIndices;
import com.cs.es.entity.BmSpfl;
import com.cs.es.mapper.BmSpflMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: CS
 * @date: 2021/5/12 下午1:57
 * @description: 初始化数据
 */
@Slf4j
@Configuration
public class InitDataConfig {

    @Autowired
    BmSpflMapper spflMapper;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    ObjectMapper objectMapper;


    @PostConstruct
    public void init() throws IOException {
        List<BmSpfl> list = spflMapper.selectAll();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Map<String, Object> mapping = elasticsearchRestTemplate.getMapping(EsIndices.BM_SPFL.getIndexName(), EsIndices.BM_SPFL.getIndexType());
        log.info(objectMapper.writeValueAsString(mapping));
        SearchQuery initQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withIndices(EsIndices.BM_SPFL.getIndexName())
                .build();
        long count = elasticsearchRestTemplate.count(initQuery);
        if (count == list.size()) {
            return;
        }
        List<IndexQuery> data = list.stream().map(p -> {
            IndexQuery indexQuery = null;
            try {
                indexQuery = new IndexQueryBuilder()
                        .withId(p.getBm())
                        .withIndexName(EsIndices.BM_SPFL.getIndexName())
                        .withType(EsIndices.BM_SPFL.getIndexType())
                        .withSource(objectMapper.writeValueAsString(p))
                        .build();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return indexQuery;
        }).collect(Collectors.toList());

        elasticsearchRestTemplate.bulkIndex(data);
    }
}
