package com.cs.es.binlog.converter;

import com.cs.es.binlog.bean.SourceTargetPair;
import org.springframework.core.convert.support.GenericConversionService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author: CS
 * @date: 2021/5/10 上午10:30
 * @description: 用于字段值之间转化
 * @see org.springframework.core.convert.converter.Converter
 * @see GenericConversionService
 */
public interface Converter<S, T> {

    /**
     * 转换
     *
     * @param source
     * @return
     */
    T convert(S source);


    /**
     * 接口实现的入参和出参的类作为对象绑定
     *
     * @return
     */
    default SourceTargetPair getKey() {
        Type[] types = this.getClass().getGenericInterfaces();
        ParameterizedType tp = (ParameterizedType) types[0];
        Type[] args = tp.getActualTypeArguments();

        Class<S> sClass = (Class<S>) args[0];
        Class<T> tClass = (Class<T>) args[1];
        return new SourceTargetPair(sClass, tClass);
    }
}
