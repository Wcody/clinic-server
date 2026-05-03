/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月29日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>系统参数组表 实体类</p>
* @datetime 2026-4-29 17:45
*/
@Getter
@Setter
@TableName(value = "bq_param_group", autoResultMap = true)
public class BqParamGroupEntity extends BQIdBaseEntity {

    /**
     * 父ID
     */
    private Integer pid;

    /**
     * 组名称
     */
    private String name;

    /**
     * 组说明
     */
    private String remark;

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
