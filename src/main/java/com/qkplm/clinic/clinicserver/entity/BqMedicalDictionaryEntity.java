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
* @description <p>医疗基础数据字典表 实体类</p>
* @datetime 2026-4-19 19:15
*/
@Getter
@Setter
@TableName(value = "bq_medical_dictionary", autoResultMap = true)
public class BqMedicalDictionaryEntity extends BQIdBaseEntity {

    /**
     * 字典类型:1用法 2频率 3单位 4剂型 5煎药方式 6过敏史
     */
    private Byte dictType;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * [B]是否常用:1是 0否
     */
    private Boolean common;

    /**
     * [B]是否删除:1是 0否
     */
    private Boolean deleted;

    /**
     * [B]药品类型:1西药 2中药
     */
    private Boolean medicineType;

    /**
     * [B]是否执行项目:1是 0否
     */
    private Boolean executionProject;

    /**
     * 天数(频率专用)
     */
    private Integer day;

    /**
     * 次数(频率专用)
     */
    private Integer time;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 行乐观锁,整数,非空,默认0
     */
    private Integer version;

    /**
     * 删除人名称,字符串,长度256
     */
    private String deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
