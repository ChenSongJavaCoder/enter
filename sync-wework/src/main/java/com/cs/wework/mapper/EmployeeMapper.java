package com.cs.wework.mapper;

import com.cs.wework.entity.Employee;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface EmployeeMapper extends Mapper<Employee>, MySqlMapper<Employee> {
}
