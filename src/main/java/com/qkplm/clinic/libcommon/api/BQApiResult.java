/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.api;

import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-12 09:41
 */
@Data
@Builder
@AllArgsConstructor
public class BQApiResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 状态码，0表示成功，非0表示失败
     */
    private int code;
    /**
     * 状态信息，code非0时返回
     */
    private String message;
    /**
     * 数据，code为0时返回
     */
    private Object data;

    /**
     * 异常类名
     */
    private String exception;

    public static BQApiResult ok(Object data) {
        return BQApiResult.builder().code(0).data(data).build();
    }

    public String toJsonString() {
        return BQJacksonUtils.toJson(this);
    }
}
