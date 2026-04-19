/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月19日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>VIEW 实体类</p>
* @datetime 2026-4-19 19:15
*/
@Getter
@Setter
@TableName(value = "view_qcr", autoResultMap = true)
public class ViewQcrEntity {

    /**
     * 内ID
     */
    private Integer uid;

    /**
     * 病案ID
     */
    private String eid;

    /**
     * -1待分类、0档案、1临床病案、2门诊病案
     */
    private Integer recordKind;

    /**
     * 病案代号
     */
    private String recordCode;

    /**
     * 患者ID
     */
    private String patientId;

    /**
     * 0普通病人
     */
    private Integer patientKind;

    /**
     * 患者代号（健康卡号）
     */
    private String patientCode;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 性别, 0未知、1男、2女
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private LocalDate dateOfBirth;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 现住址
     */
    private String address;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 民族
     */
    private String ethnicity;

    /**
     * 婚姻状况, 0未知、1未婚、2已婚、3离异、4丧偶
     */
    private Integer maritalStatus;

    /**
     * [B]是否药物过敏, 0否、1是
     */
    private Boolean hasAllergy;

    /**
     * 药物过敏
     */
    private String allergicDrug;

    /**
     * 联系人姓名
     */
    private String contactPerson;

    /**
     * 联系人关系
     */
    private String contactRelation;

    /**
     * 联系人电话
     */
    private String contactPhoneNumber;

    /**
     * 文件来源：翻拍入库、即时采集、手动创建
     */
    private String recordSource;

    /**
     * 住院次数
     */
    private Integer admissionCount;

    /**
     * [B]是否归档, 0否、1是
     */
    private Boolean hasArchived;

    /**
     * 住院日期
     */
    private LocalDateTime admissionDate;

    /**
     * 出院日期
     */
    private LocalDateTime dischargeDate;

    /**
     * 住院科室
     */
    private String department;

    /**
     * 出院科室
     */
    private String dischargeDepartment;

    /**
     * 住院天数
     */
    private Integer lengthOfStay;

    /**
     * 主管医生
     */
    private String attendingDoctor;

    /**
     * 主要诊断编码
     */
    private String mainDiagnosisCode;

    /**
     * 主要诊断名称
     */
    private String mainDiagnosisName;

    /**
     * 主要手术编码
     */
    private String mainSurgeryCode;

    /**
     * 主要手术名称
     */
    private String mainSurgeryName;

    /**
     * [B]是否死亡, 0否、1是
     */
    private Boolean hasDeceased;

    /**
     * 上架号
     */
    private String shelfNumber;

    /**
     * 病区编码
     */
    private String wardCode;

    /**
     * 病区名称
     */
    private String wardName;

    /**
     * [B]是否打印, 0否、1是
     */
    private Boolean hasPrinted;

    /**
     * [B]是否质检, 0否、1是
     */
    private Boolean hasQualityChecked;

    /**
     * [B]是否完整，0否，1是
     */
    private Boolean hasCompleted;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 乐观锁
     */
    private Integer version;

    /**
     * 逻辑删除, 0未删除、1已删除
     */
    private Integer deleted;

    /**
     * [B]启用禁用, 0禁用、1启用
     */
    private Boolean status;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新者
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * [B]是否护士质控，0否，1是
     */
    private Boolean hasNurse;

    /**
     * [B]是否医生质控，0否，1是
     */
    private Boolean hasDoctor;

    /**
     * [B]是否终末质控，0否，1是
     */
    private Boolean hasFinal;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 追踪内容
     */
    private String content;

    /**
     * 备注
     */
    private String trackRemark;

    /**
     * 创建时间
     */
    private LocalDateTime trackTime;
}
