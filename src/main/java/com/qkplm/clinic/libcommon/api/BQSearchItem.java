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
 * @datetime: 2024-06-13 17:57
 */
@Data
public class BQSearchItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 字段
     */
    private String field;
    /**
     * 操作符
     */
    private String operator;
    /**
     * 值
     */
    private Object value;
    /**
     * 值2，在between和not between时使用
     */
    private Object value2;
}
