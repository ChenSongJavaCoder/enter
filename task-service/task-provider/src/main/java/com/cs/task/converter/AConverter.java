package com.cs.task.converter;

import com.cs.task.pojo.ARequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author: CS
 * @date: 2021/4/27 下午3:53
 * @description:
 */
@Component
public class AConverter implements Converter<String, ARequest> {
    @Override
    public ARequest convert(String source) {
        return new ARequest();
    }
}
