/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月29日
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
* @description <p>系统参数数据表 实体类</p>
* @datetime 2026-4-29 17:45
*/
@Getter
@Setter
@TableName(value = "bq_param_data", autoResultMap = true)
public class BqParamDataEntity extends BQIdBaseEntity {

    /**
     * 参数项ID
     */
    private Integer paramId;

    /**
     * 用户ID，系统用0，不同租户的系统设置，用租户ID
     */
    private String userId;

    /**
     * [O]参数值
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ObjectNode paramValue;

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
