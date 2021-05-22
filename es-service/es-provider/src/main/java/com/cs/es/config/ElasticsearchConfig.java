package com.cs.es.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

/**
 * @author: CS
 * @date: 2021/5/12 上午11:16
 * @description: 注入ElasticsearchRestTemplate
 */
@Configuration
public class ElasticsearchConfig {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Bean
    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
        return new ElasticsearchRestTemplate(restHighLevelClient);
    }
}
