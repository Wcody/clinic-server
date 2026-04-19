/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月19日
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
* @description <p>枚举子项表 实体类</p>
* @datetime 2026-4-19 19:15
*/
@Getter
@Setter
@TableName(value = "bq_enum_item", autoResultMap = true)
public class BqEnumItemEntity extends BQIdBaseEntity {

    /**
     * 父ID
     */
    private String pid;

    /**
     * 枚举名称
     */
    private String enumName;

    /**
     * 枚举类型
     */
    private Integer enumType;

    /**
     * 字符串值
     */
    private String enumValue;

    /**
     * 枚举说明
     */
    private String enumDesc;

    /**
     * 顺序值
     */
    private Integer seq;

    /**
     * [B]启用禁用
     */
    private Boolean status;

    /**
     * 乐观锁
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

    /**
     * 逻辑删除
     */
    private Integer delete;
}
