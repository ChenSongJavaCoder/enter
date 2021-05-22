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
 * @Description:
 */
public class BigDecimal4Serializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (Objects.nonNull(bigDecimal)) {
            jsonGenerator.writeNumber(bigDecimal.setScale(4, RoundingMode.HALF_UP));
        }
    }
}
