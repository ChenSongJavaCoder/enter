package com.cs.es;

import com.cs.es.common.EsIndices;
import com.cs.es.common.EsRestService;
import com.cs.es.entity.BmSpfl;
import com.cs.es.mapper.BmSpflMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.util.CollectionUtils;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author chenS
 * @Date 2019-12-04 14:40
 * @Description
 **/
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.cs.es.mapper")
public class ElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }

    @Autowired
    BmSpflMapper spflMapper;

    @Autowired
    EsRestService esRestService;

    @Autowired
    RestHighLevelClient client;

    @Autowired
    ObjectMapper objectMapper;


    @PostConstruct
    public void init() throws IOException {
        List<BmSpfl> list = spflMapper.selectAll();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        CountRequest countRequest = new CountRequest(EsIndices.BM_SPFL.getIndexName());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        countRequest.source(searchSourceBuilder);
        CountResponse response = client.count(countRequest, RequestOptions.DEFAULT);
        if (list.size() == response.getCount()) {
            return;
        }
        List<String> json = list.stream().map(p -> {
            try {
                return objectMapper.writeValueAsString(p);
            } catch (JsonProcessingException e) {
                return null;
            }
        }).collect(Collectors.toList());

        esRestService.bulkCreateDocument(EsIndices.BM_SPFL, json);
    }
}
