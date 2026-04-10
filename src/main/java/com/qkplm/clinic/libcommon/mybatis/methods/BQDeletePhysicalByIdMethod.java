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

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-29 21:41
 */
public class BQDeletePhysicalByIdMethod extends AbstractMethod {

    protected BQDeletePhysicalByIdMethod(String methodName) {
        super(methodName);
    }

    public BQDeletePhysicalByIdMethod() {
        this("deletePhysicalById");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = String.format("DELETE FROM %s WHERE %s=#{id}", tableInfo.getTableName(), tableInfo.getKeyColumn());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
        return this.addDeleteMappedStatement(mapperClass, methodName, sqlSource);
    }
}
