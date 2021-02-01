package com.cs.common.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * @Author chenS
 * @Date 2019-11-05 21:54
 * @Description 真是个灵活的小胖子
 **/
public interface SmartConverter<S, T, C extends ConverterConfig> extends Converter<S, T> {

    /**
     * 为对象间转换加入配置
     *
     * @param s 需要被转换的对象
     * @param c
     * @return
     */
    T convert(S s, C c);
}
