/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.mybatis.base;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-29 21:33
 */
public class BaqiServiceImpl<M extends IBaqiMapper<T>, T> extends ServiceImpl<M, T> implements IBaqiService<T> {
    private final ConversionService conversionService = DefaultConversionService.getSharedInstance();
    public boolean removePhysicalById(Serializable id) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.getEntityClass());
        return (tableInfo.isWithLogicDelete() && tableInfo.isWithUpdateFill()) ? this.removePhysicalById(id, true) : SqlHelper.retBool(this.getBaseMapper().deletePhysicalById(id));
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removePhysicalByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        } else {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(this.getEntityClass());
            return tableInfo.isWithLogicDelete() && tableInfo.isWithUpdateFill() ? this.removePhysicalBatchByIds(list, true) : SqlHelper.retBool(this.getBaseMapper().deletePhysicalBatchIds(list));
        }
    }

    public boolean removePhysicalById(Serializable id, boolean useFill) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
        if (useFill && tableInfo.isWithLogicDelete() && !this.entityClass.isAssignableFrom(id.getClass())) {
            T instance = tableInfo.newInstance();
            Object value = tableInfo.getKeyType() != id.getClass() ? this.conversionService.convert(id, tableInfo.getKeyType()) : id;
            tableInfo.setPropertyValue(instance, tableInfo.getKeyProperty(), new Object[]{value});
            return this.removePhysicalById(instance);
        } else {
            return SqlHelper.retBool(this.getBaseMapper().deletePhysicalById(id));
        }
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removePhysicalBatchByIds(Collection<?> list, int batchSize) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
        return this.removePhysicalBatchByIds(list, batchSize, tableInfo.isWithLogicDelete() && tableInfo.isWithUpdateFill());
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removePhysicalBatchByIds(Collection<?> list, int batchSize, boolean useFill) {
        String sqlStatement = this.mapperClass.getName().concat(".").concat("deletePhysicalById");
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
        return this.executeBatch(list, batchSize, (sqlSession, e) -> {
            sqlSession.update(sqlStatement, e);
        });
    }
}
