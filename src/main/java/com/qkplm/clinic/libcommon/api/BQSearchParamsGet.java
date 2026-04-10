/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.api;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: Wcke
 * @description: 用于Get模式的查询参数
 * @datetime: 2024-06-13 20:11
 */
@Data
public class BQSearchParamsGet<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public BQSearchParamsGet() {
        size = -1L;
        page = 1L;
        orders = null;
        filters = null;
    }
    /**
     * 分页大小
     */
    private Long size;
    /**
     * 当前页
     */
    private Long page;
    /**
     * 排序字段列白哦
     */
    private String orders;
    /**
     * 搜索条件列表
     */
    private String filters;

    public BQSearchParams<T> toSearchParams() {
        BQSearchParams<T> params = new BQSearchParams<>();
        params.setSize(size);
        params.setPage(page);
        params.setOrders(orders);
        params.setFilters(filters);
        return params;
    }
}
