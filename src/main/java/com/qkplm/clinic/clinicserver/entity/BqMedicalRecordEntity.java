/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月23日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>门诊就诊记录表 实体类</p>
* @datetime 2026-4-23 11:50
*/
@Getter
@Setter
@TableName(value = "bq_medical_record", autoResultMap = true)
public class BqMedicalRecordEntity extends BQIdBaseEntity {

    /**
     * 挂号ID（无挂号为空）
     */
    private Integer regId;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 就诊时患者身高，单位cm
     */
    private BigDecimal height;

    /**
     * 就诊时患者体重，单位kg
     */
    private BigDecimal weight;

    /**
     * 接诊医生ID
     */
    private Integer doctorId;

    /**
     * 主诉
     */
    private String chiefComplaint;

    /**
     * 现病史
     */
    private String presentIllness;

    /**
     * 既往史
     */
    private String pastHistory;

    /**
     * 体格检查
     */
    private String physicalExam;

    /**
     * 诊断结果
     */
    private String diagnosis;

    /**
     * 诊断编码ID串(逗号分隔)
     */
    private String diagnosisIds;

    /**
     * 医嘱
     */
    private String advice;

    /**
     * 就诊时间
     */
    private LocalDateTime seeTime;

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
