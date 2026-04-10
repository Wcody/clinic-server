/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.mybatis.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Collection;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-29 22:00
 */
public class BQDeletePhysicalBatchIdsMethod extends AbstractMethod {
    protected BQDeletePhysicalBatchIdsMethod(String methodName) {
        super(methodName);
    }

    public BQDeletePhysicalBatchIdsMethod() {
        this("deletePhysicalBatchIds");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 定义物理删除 SQL 语句，批量删除
        String sql = String.format("<script>DELETE FROM %s WHERE %s IN <foreach collection=\"coll\" item=\"id\" open=\"(\" separator=\",\" close=\")\">#{id}</foreach></script>",
                tableInfo.getTableName(), tableInfo.getKeyColumn());
        // 创建 SqlSource
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Collection.class);
        // 添加 MappedStatement
        return this.addDeleteMappedStatement(mapperClass, methodName, sqlSource);
    }
}
