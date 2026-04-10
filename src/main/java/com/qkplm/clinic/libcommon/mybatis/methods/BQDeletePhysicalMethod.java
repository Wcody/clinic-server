/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.mybatis.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.sql.Wrapper;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-29 22:00
 */
public class BQDeletePhysicalMethod extends AbstractMethod {
    protected BQDeletePhysicalMethod(String methodName) {
        super(methodName);
    }

    public BQDeletePhysicalMethod() {
        this("deletePhysical");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 定义物理删除 SQL 语句，使用 Wrapper<T> 条件
        String sql = String.format("DELETE FROM %s %s", tableInfo.getTableName(), SqlScriptUtils.convertWhere("#{ew.sqlSegment}"));
        // 创建 SqlSource
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Wrapper.class);
        // 添加 MappedStatement
        return this.addDeleteMappedStatement(mapperClass, methodName, sqlSource);
    }
}
