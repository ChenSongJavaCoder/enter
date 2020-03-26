package com.cs.wework.base;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@RegisterMapper
public interface TkBaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
