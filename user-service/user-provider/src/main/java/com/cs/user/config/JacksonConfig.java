package com.cs.user.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @Author chenS
 * @Date 2019-11-05 22:35
 * @Description TODO 暂不处理
 **/
//@Configuration
public class JacksonConfig {

//    @Bean
//    @Primary
//    @ConditionalOnMissingBean(ObjectMapper.class)
public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
    ObjectMapper objectMapper = builder.createXmlMapper(false).build();
    //通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化，属性为NULL 不序列化
    // 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
    // Include.Include.ALWAYS 默认
    // Include.NON_DEFAULT 属性为默认值不序列化
    // Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。这样对移动端会更省流量
    // Include.NON_NULL 属性为NULL 不序列化,就是为null的字段不参加序列化
    //objectMapper.setSerializationInclusion(Include.NON_EMPTY);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return objectMapper;
}

}
