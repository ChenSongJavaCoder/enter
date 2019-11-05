package com.cs.common.config;

/**
 * @Author chenS
 * @Date 2019-11-05 22:35
 * @Description
 **/
//@Configuration
//public class JacksonConfig {
//
//    @Bean
//    @Primary
//    @ConditionalOnMissingBean(ObjectMapper.class)
//    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
//        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//        //通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化，属性为NULL 不序列化
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        return objectMapper;
//    }
//
//}
