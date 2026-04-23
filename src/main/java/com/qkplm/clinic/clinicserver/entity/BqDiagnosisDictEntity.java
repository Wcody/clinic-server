/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月23日
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
* @description <p>诊断字典表 实体类</p>
* @datetime 2026-4-23 11:50
*/
@Getter
@Setter
@TableName(value = "bq_diagnosis_dict", autoResultMap = true)
public class BqDiagnosisDictEntity extends BQIdBaseEntity {

    /**
     * ICD编码
     */
    private String diagnosisCode;

    /**
     * 诊断名称
     */
    private String diagnosisName;

    /**
     * 拼音码
     */
    private String pinyin;

    /**
     * [B]状态:1启用 0禁用
     */
    private Boolean status;

    /**
     * 行乐观锁
     */
    private Integer version;

    /**
     * [B]逻辑删除标记:0未删除 1已删除
     */
    private Boolean deleted;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * 删除人名称
     */
    private String deletedBy;
}
