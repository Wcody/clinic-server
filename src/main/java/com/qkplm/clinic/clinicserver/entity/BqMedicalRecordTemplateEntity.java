/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月22日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>病历模板表 实体类</p>
* @datetime 2026-4-22 2:10
*/
@Getter
@Setter
@TableName(value = "bq_medical_record_template", autoResultMap = true)
public class BqMedicalRecordTemplateEntity extends BQIdBaseEntity {

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板类型，1个人，2诊所
     */
    private Integer type;

    /**
     * 主诉
     */
    private String complaint;

    /**
     * 现病史
     */
    private String historyOfPresentIllness;

    /**
     * 既往史
     */
    private String pastHistory;

    /**
     * 个人史
     */
    private String personalHistory;

    /**
     * 家族史
     */
    private String familyHistory;

    /**
     * 婚育史
     */
    private String obstericalHistory;

    /**
     * 体温
     */
    private BigDecimal bodyTemperature;

    /**
     * 收缩压(高压)
     */
    private Integer bloodPressureHight;

    /**
     * 舒张压(低压)
     */
    private Integer bloodPressureLow;

    /**
     * 心率
     */
    private Integer heartRate;

    /**
     * 呼吸频率
     */
    private Integer breathRate;

    /**
     * 其他检查
     */
    private String otherExamine;

    /**
     * 治疗建议
     */
    private String treatmentRecommendation;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作时间
     */
    private LocalDate operatorTime;

    /**
     * [B]状态:1启用 0禁用
     */
    private Boolean status;

    /**
     * 记录创建时间
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
     * [B]逻辑删除标记,0表示false,1表示true
     */
    private Boolean deleted;

    /**
     * 删除人名称,字符串,长度256
     */
    private String deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * 一级目录ID
     */
    private Integer oneLevel;

    /**
     * 二级目录ID
     */
    private Integer twoLevel;

    /**
     * [B]是否为目录,0否，1是
     */
    private Boolean hasCategory;

    /**
     * 所在父目录ID
     */
    private Integer parentId;
}
