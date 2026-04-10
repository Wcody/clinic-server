/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.annotation;

import com.qkplm.clinic.libcommon.enums.BQLogTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-24 21:25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BQLogMark {
    /**
     * 所属模块
     */
    String module() default "";

    /**
     * 操作
     */
    String operation() default "";

    /**
     * 日志类别
     */
    BQLogTypeEnum loginType() default BQLogTypeEnum.OPERATION;
}
