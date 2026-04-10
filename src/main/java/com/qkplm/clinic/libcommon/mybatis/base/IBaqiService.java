/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-29 20:27
 */
public interface IBaqiService<T> extends IService<T> {
    default boolean removePhysicalById(Serializable id) {
        return SqlHelper.retBool(this.getBaseMapper().deletePhysicalById(id));
    }

    default boolean removePhysicalById(Serializable id, boolean useFill) {
        throw new UnsupportedOperationException("不支持的方法!");
    }

    default boolean removePhysicalById(T entity) {
        return SqlHelper.retBool(this.getBaseMapper().deletePhysicalByEntity(entity));
    }

    default boolean removePhysicalByMap(Map<String, Object> columnMap) {
        Assert.notEmpty(columnMap, "error: columnMap must not be empty", new Object[0]);
        return SqlHelper.retBool(this.getBaseMapper().deletePhysicalByMap(columnMap));
    }

    default boolean removePhysical(Wrapper<T> queryWrapper) {
        return SqlHelper.retBool(this.getBaseMapper().deletePhysical(queryWrapper));
    }

    default boolean removePhysicalByIds(Collection<?> list) {
        return !CollectionUtils.isEmpty(list) && SqlHelper.retBool(this.getBaseMapper().deletePhysicalBatchIds(list));
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    default boolean removePhysicalByIds(Collection<?> list, boolean useFill) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        } else {
            return useFill ? this.removePhysicalBatchByIds(list, true) : SqlHelper.retBool(this.getBaseMapper().deletePhysicalBatchIds(list));
        }
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    default boolean removePhysicalBatchByIds(Collection<?> list) {
        return this.removePhysicalBatchByIds(list, 1000);
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    default boolean removePhysicalBatchByIds(Collection<?> list, boolean useFill) {
        return this.removePhysicalBatchByIds(list, 1000, useFill);
    }

    default boolean removePhysicalBatchByIds(Collection<?> list, int batchSize) {
        throw new UnsupportedOperationException("不支持的方法!");
    }

    default boolean removePhysicalBatchByIds(Collection<?> list, int batchSize, boolean useFill) {
        throw new UnsupportedOperationException("不支持的方法!");
    }

    IBaqiMapper<T> getBaseMapper();
}
