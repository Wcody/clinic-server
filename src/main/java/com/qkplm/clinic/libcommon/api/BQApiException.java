/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-12 09:41
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class BQApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 错误码，非0值
     */
    private int code;

    /**
     * 异常类名
     */
    private String exception;

    /**
     * 构造函数
     */
    public BQApiException() {
        super("");
        this.code = 1;
    }

    public BQApiException(String message) {
        super(message);
        this.code = 1;
    }

    public BQApiException(int code, String message) {
        super(message);
        this.code = code;
        this.exception = null;
    }

    public BQApiException(int code, String message, String exception) {
        super(message);
        this.code = code;
        this.exception = exception;
    }
}
