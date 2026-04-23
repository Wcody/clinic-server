/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月23日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>系统日志 实体类</p>
* @datetime 2026-4-23 11:50
*/
@Getter
@Setter
@TableName(value = "bq_log", autoResultMap = true)
public class BqLogEntity extends BQIdBaseEntity {

    /**
     * 操作账号
     */
    private String account;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 日志类型
     */
    private Integer logType;

    /**
     * 操作
     */
    private String operation;

    /**
     * 请求方式POST或GET
     */
    private String method;

    /**
     * [B]0是失败，1是成功
     */
    private Boolean status;

    /**
     * 失败时的异常信息
     */
    private String exceptionInfo;

    /**
     * 请求开始的时间
     */
    private LocalDateTime beginTime;

    /**
     * 请求完成的时间
     */
    private LocalDateTime endTime;

    /**
     * 请求耗时(毫秒)
     */
    private Long costTime;

    /**
     * 请求的USERAGENT
     */
    private String userAgent;

    private String platform;

    /**
     * 请求者的操作系统
     */
    private String os;

    /**
     * 请求者用的浏览器
     */
    private String browser;

    /**
     * 请求者所用IP
     */
    private String requestIp;

    /**
     * 请求者所在的地址
     */
    private String requestAddress;

    /**
     * 请求的URI
     */
    private String requestUri;

    /**
     * [O]请求的URI参数
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ObjectNode requestParams;

    /**
     * [O]请求头
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ObjectNode requestHeader;

    /**
     * [O]请求体
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ObjectNode requestBody;

    /**
     * [O]响应头
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ObjectNode responseHeader;

    /**
     * [O]响应体
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ObjectNode responseBody;

    /**
     * 行乐观锁
     */
    private Integer version;

    /**
     * [B]逻辑删除标记
     */
    private Boolean deleted;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
