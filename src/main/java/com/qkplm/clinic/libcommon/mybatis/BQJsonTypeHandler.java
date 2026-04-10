/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.mybatis;

import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-26 21:47
 */
//@MappedTypes({ObjectNode.class, ArrayNode.class})
@MappedTypes({Object.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class BQJsonTypeHandler<T> extends BaseTypeHandler<T> {
    private final Class<T> type;

    public BQJsonTypeHandler(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type参数不能为空");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, BQJacksonUtils.toJson(parameter));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonString = rs.getString(columnName);
        return BQJacksonUtils.jsonToBean(jsonString, type);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonString = rs.getString(columnIndex);
        return BQJacksonUtils.jsonToBean(jsonString, type);

    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonString = cs.getString(columnIndex);
        return BQJacksonUtils.jsonToBean(jsonString, type);
    }
}
