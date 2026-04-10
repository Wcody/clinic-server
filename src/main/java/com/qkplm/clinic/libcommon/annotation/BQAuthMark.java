/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-24 13:39
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BQAuthMark {
    /**
     * 该接口的权限标识，根据方法名和tag拼接成tag:methodName的形式
     */
    String tag() default "";

    /**
     * 按钮标识，对应前端页面的操作按钮标识
     */
    String[] buttons() default {};

    /**
     * 版本
     */
    String version() default "v1";

    /**
     * 需要认证，就是访问前需要先登录，不需要认证，授权开关无效
     */
    boolean needAuth() default true;

    /**
     * 需要授权，就是认证后，需要授权才能访问
     */
    boolean needGrant() default true;
}
