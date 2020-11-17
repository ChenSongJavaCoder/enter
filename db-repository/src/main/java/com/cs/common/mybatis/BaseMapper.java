package com.cs.common.mybatis;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @ClassName: BaseMapper
 * @Author: CS
 * @Date: 2019/11/2 10:21
 * @Description: mapper基础接口
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
