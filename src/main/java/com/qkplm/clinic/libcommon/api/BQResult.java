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
 * @description:
 * @datetime: 2024-06-12 16:32
 */
@Data
public class BQResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 数据，不需要返回状态码时用该对象封装
     */
    private T data;
    public BQResult(T data) {
        this.data = data;
    }
}
