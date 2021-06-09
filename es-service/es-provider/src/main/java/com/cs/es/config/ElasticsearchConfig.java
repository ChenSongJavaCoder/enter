package com.cs.es.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;
import org.springframework.data.mapping.MappingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        return new ElasticsearchRestTemplate(restHighLevelClient, new CustomEntityMapper());
    }

    public static class CustomEntityMapper implements EntityMapper {

        private final ObjectMapper objectMapper;

        public CustomEntityMapper() {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            objectMapper.registerModule(new CustomGeoModule());
            objectMapper.registerModule(new JavaTimeModule());
        }

        @Override
        public String mapToString(Object object) throws IOException {
            return objectMapper.writeValueAsString(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
            return objectMapper.readValue(source, clazz);
        }

        @Override
        public Map<String, Object> mapObject(Object source) {
            try {
                return objectMapper.readValue(mapToString(source), HashMap.class);
            } catch (IOException e) {
                throw new MappingException(e.getMessage(), e);
            }
        }

        @Override
        public <T> T readObject(Map<String, Object> source, Class<T> targetType) {
            try {
                return mapToObject(mapToString(source), targetType);
            } catch (IOException e) {
                throw new MappingException(e.getMessage(), e);
            }
        }
    }
}
