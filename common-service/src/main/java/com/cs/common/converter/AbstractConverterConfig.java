package com.cs.common.converter;

/**
 * @Author chenS
 * @Date 2019-11-05 21:53
 * @Description
 **/
public abstract class AbstractConverterConfig<T> {

    public abstract T defaultConfig();

    public abstract String[] ignoreProperties();

}
