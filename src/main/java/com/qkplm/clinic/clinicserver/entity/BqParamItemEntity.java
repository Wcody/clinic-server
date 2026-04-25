/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月26日
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
* @description <p>系统参数项表 实体类</p>
* @datetime 2026-4-26 7:17
*/
@Getter
@Setter
@TableName(value = "bq_param_item", autoResultMap = true)
public class BqParamItemEntity extends BQIdBaseEntity {

    /**
     * 组ID
     */
    private Integer pid;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数类型，1数字，2字符串，3图片，4唯一性，5完整性，6齐套性，7水印
     */
    private Integer paramType;

    /**
     * [O]参数配置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ObjectNode paramConfig;

    /**
     * [O]参数值
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ObjectNode paramValue;

    /**
     * 参数说明
     */
    private String paramDesc;

    /**
     * 顺序值
     */
    private Integer seq;

    /**
     * [B]启用禁用
     */
    private Boolean status;

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
