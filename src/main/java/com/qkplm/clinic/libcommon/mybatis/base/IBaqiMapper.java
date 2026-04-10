/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-29 19:34
 */
public interface IBaqiMapper<T> extends BaseMapper<T> {
    /**
     * 忽略逻辑删除标记，物理删除，删除后数据不可恢复
     */
    int deletePhysicalById(Serializable id);

    /**
     * 忽略逻辑删除标记，物理删除，删除后数据不可恢复
     */
    int deletePhysicalByEntity(T entity);

    /**
     * 忽略逻辑删除标记，物理删除，删除后数据不可恢复
     */
    default int deletePhysicalByMap(Map<String, Object> columnMap) {
        QueryWrapper<T> queryWrapper = Wrappers.query();
        return this.deletePhysical(queryWrapper.allEq(columnMap));
    }

    /**
     * 忽略逻辑删除标记，物理删除，删除后数据不可恢复
     */
    int deletePhysical(@Param("ew") Wrapper<T> queryWrapper);

    /**
     * 忽略逻辑删除标记，物理删除，删除后数据不可恢复
     */
    int deletePhysicalBatchIds(@Param("coll") Collection<?> idList);
}
