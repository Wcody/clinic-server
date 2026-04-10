/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.qkplm.clinic.libcommon.mybatis.methods.BQDeletePhysicalBatchIdsMethod;
import com.qkplm.clinic.libcommon.mybatis.methods.BQDeletePhysicalByEntityMethod;
import com.qkplm.clinic.libcommon.mybatis.methods.BQDeletePhysicalByIdMethod;
import com.qkplm.clinic.libcommon.mybatis.methods.BQDeletePhysicalMethod;
import org.apache.ibatis.session.Configuration;

import java.util.List;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-29 22:11
 */
public class BQMapperMethodsInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Configuration configuration, Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(configuration, mapperClass, tableInfo);
        methodList.add(new BQDeletePhysicalByIdMethod());
        methodList.add(new BQDeletePhysicalBatchIdsMethod());
        methodList.add(new BQDeletePhysicalByEntityMethod());
        methodList.add(new BQDeletePhysicalMethod());
        return methodList;
    }
}
