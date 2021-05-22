package com.cs.es.service;

import com.cs.es.common.EsIndices;
import com.cs.es.model.DemoTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;

import java.util.Map;

/**
 * @author: CS
 * @date: 2021/5/20 上午11:33
 * @description:
 */
public abstract class EsBaseService {

    @Autowired
    protected ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    protected ObjectMapper objectMapper;


    protected boolean checkIndexExist(String index) {
        return elasticsearchRestTemplate.indexExists(index);
    }

    protected Map<String, Object> getIndexMapping(String index, String type) {
        return elasticsearchRestTemplate.getMapping(index, type);
    }

    protected boolean createIndex(String index) {
        return elasticsearchRestTemplate.createIndex(index);
    }

    public String updateDocument() {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.retryOnConflict(1);
        updateRequest.doc("name","cs-250");

        UpdateQuery updateQuery = new UpdateQueryBuilder()
//                .withClass()
                .withId("2FbAz24BaA_ypEOp4wip")
                .withDoUpsert(true)
                .withIndexName(EsIndices.DEMO.getIndexName())
                .withType(EsIndices.DEMO.getIndexType())
                .withUpdateRequest(updateRequest)
                .build();
        UpdateResponse response = elasticsearchRestTemplate.update(updateQuery);
        try {
           return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
