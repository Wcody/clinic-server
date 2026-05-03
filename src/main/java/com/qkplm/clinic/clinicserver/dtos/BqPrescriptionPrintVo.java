/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
*/
package com.qkplm.clinic.clinicserver.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Wcke
 * @description 处方打印 JOIN 查询结果 VO，一次查询聚合处方/挂号/患者/病历/枚举所需字段
 */
@Data
public class BqPrescriptionPrintVo {

    // ---- bq_prescription ----
    private Integer id;
    private String prescNo;
    private Integer prescType;
    private BigDecimal totalPrice;
    /** 剂数（中药） */
    private Integer doseAmount;
    /** 医嘱（中药） */
    private String recommendation;
    /** 用法类型ID（中药） */
    private Integer usageType;
    /** 频次ID（中药） */
    private Integer frequence;
    private Integer days;
    private Integer regId;

    // ---- bq_registration ----
    private String patientName;
    private String gender;
    private Integer firstAge;
    private Integer lastAge;
    /** 年龄类型：1=岁+月  2=月+天 */
    private Integer ageType;
    private LocalDateTime orderTime;
    private String doctor;
    private Integer registrationNo;

    // ---- bq_patient ----
    private String idCard;
    private String phone;
    private String address;
    private String allergicHistory;
    private String archiveNo;

    // ---- bq_medical_record ----
    private String diagnosis;
    private String treatmentSuggest;

    // ---- bq_enum_item（中药频次/用法文本） ----
    private String usageTypeName;
    private String frequenceName;
}
