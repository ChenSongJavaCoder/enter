package com.cs.common.converter;

/**
 * @Author chenS
 * @Date 2019-11-05 21:53
 * @Description 使用转换的配置，作用于不同使用场景下的对象间属性赋值，目前功能上作用于DO与DTO之间的转换
 **/
public interface ConverterConfig<T> {

    /**
     * 默认的转换配置
     *
     * @return
     */
    T defaultConfig();

    /**
     * 忽略的转化配置
     *
     * @return
     */
    default String[] ignoreProperties() {
        return new String[]{};
    }

}
