package com.cs.common.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * @Author chenS
 * @Date 2019-11-05 21:54
 * @Description 真是个灵活的小胖子
 **/
public interface SmartConverter<S, T, C extends AbstractConverterConfig> extends Converter<S, T> {

    T converter(S s, C config);
}
