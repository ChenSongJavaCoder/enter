package com.cs.es.binlog.converter.impl;

import com.cs.es.binlog.converter.Converter;
import com.cs.es.model.SexEnum;

import java.util.Objects;

/**
 * @author: CS
 * @date: 2021/5/25 下午6:03
 * @description:
 */
public class IntegerToSexEnumConverter implements Converter<Integer, SexEnum> {

    @Override
    public SexEnum convert(Integer source) {
        if (Objects.isNull(source)) {
            return SexEnum.未知;
        }
        switch (source) {
            case 1:
                return SexEnum.男;
            case 2:
                return SexEnum.女;
            case 3:
                return SexEnum.其他;
            default:
                return SexEnum.未知;
        }
    }
}
