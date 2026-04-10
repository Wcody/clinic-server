/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.api;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-13 18:03
 */
@Data
public class BQSearchResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 分页大小
     */
    private Long size;
    /**
     * 当前页
     */
    private Long current;
    /**
     * 总页数
     */
    private Long total;
    /**
     * 当前页的记录列表
     */
    private List<T> list;
}
