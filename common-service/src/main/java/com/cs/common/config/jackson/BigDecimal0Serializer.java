package com.cs.common.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @Author: CS
 * @Date: 2020/10/31 16:10
 * @Description: BigDecimal 序列化格式处理 用法： @JsonSerialize(using = BigDecimal0Serializer.class)
 */
public class BigDecimal0Serializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (Objects.nonNull(bigDecimal)) {
            // 此处可自定义输出格式
            jsonGenerator.writeNumber(bigDecimal.setScale(0, RoundingMode.HALF_UP));
        }
    }
}
