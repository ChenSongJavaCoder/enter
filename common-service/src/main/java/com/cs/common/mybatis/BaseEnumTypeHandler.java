package com.cs.common.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.springframework.util.ReflectionUtils;
import sun.jvm.hotspot.debugger.cdbg.EnumType;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @Author chenS
 * @Date 2019-11-06 10:41
 * @Description TODO 待验证  enumtype 可能错误
 **/
@MappedJdbcTypes(JdbcType.INTEGER)
//@MappedTypes(value = {StatusEnum.class, AuthEnum.class})
public class BaseEnumTypeHandler extends BaseTypeHandler<EnumType> {

    private Class<EnumType> types;

    public BaseEnumTypeHandler(Class<EnumType> types) {
        this.types = types;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, EnumType parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getNumEnumerates());
    }

    @Override
    public EnumType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int id = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return getEnumType(id);
        }
    }

    @Override
    public EnumType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int id = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return getEnumType(id);
        }
    }

    private EnumType getEnumType(int id) {
        try {
            Method valueOfType = Arrays.stream(types.getDeclaredMethods())
                    .filter(m -> m.getName().equals("valueOfType"))
                    .findFirst()
                    .orElse(null);
            return (EnumType) ReflectionUtils.invokeMethod(valueOfType, types.getEnumConstants()[0], id);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot convert to " + types.getName() + " by ordinal value.", ex);
        }
    }

    @Override
    public EnumType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int id = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            return getEnumType(id);
        }
    }
}
