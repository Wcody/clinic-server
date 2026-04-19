/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月19日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>病案表 实体类</p>
* @datetime 2026-4-19 19:15
*/
@Getter
@Setter
@TableName(value = "tb_record", autoResultMap = true)
public class TbRecordEntity extends BQEidBaseEntity {

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

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
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * [B]启用禁用, 0禁用、1启用
     */
    private Boolean status;

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
     * 医疗付费方式
     */
    private String payType;

    /**
     * 患者年龄（岁）
     */
    private Integer ageYear;

    /**
     * 患者年龄（天）
     */
    private Integer ageDay;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * 出生地址
     */
    private String birthAddress;

    /**
     * 籍贯省（自治区、直辖市）
     */
    private String nativeProvince;

    /**
     * 户口地址
     */
    private String hukouAddress;

    /**
     * 户口地址邮政编码
     */
    private String hukouZipCode;

    /**
     * 现住址
     */
    private String liveAddress;

    /**
     * 现住址电话
     */
    private String livePhone;

    /**
     * 现住址邮编
     */
    private String liveZipCode;

    /**
     * 工作单位及地址
     */
    private String workUnitAddress;

    /**
     * 工作单位电话
     */
    private String workUnitPhone;

    /**
     * 工作单位邮政编码
     */
    private String workUnitZipCode;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人关系
     */
    private String contactRelationship;

    /**
     * 联系人地址
     */
    private String contactAddress;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 是否为日间手术
     */
    private String hasDaySurgery;

    /**
     * 入院途径
     */
    private String admissionRoute;

    /**
     * 入院科别
     */
    private String admissionDepartment;

    /**
     * 入院病房
     */
    private String admissionWard;

    /**
     * 转科科别
     */
    private String transferDepartment;

    /**
     * 出院病房
     */
    private String dischargeWard;

    /**
     * 实际住院（天）
     */
    private Integer actualHospitalizationDays;

    /**
     * 入院时情况
     */
    private String admissionCondition;

    /**
     * 入院诊断编码
     */
    private String admissionDiagnosisCode;

    /**
     * 入院诊断名称
     */
    private String admissionDiagnosisName;

    /**
     * 入院后确诊日期
     */
    private LocalDate postAdmissionConfirmationDate;

    /**
     * 出院主要诊断编码
     */
    private String primaryDiagnosisCode;

    /**
     * 出院主要诊断名称
     */
    private String primaryDiagnosisName;

    /**
     * 出院主要诊断入院病情
     */
    private String primaryDiagnosisAdmissionCondition;

    /**
     * 主要诊断出院情况
     */
    private String primaryDiagnosisDischargeCondition;

    /**
     * 病理诊断编码
     */
    private String pathologyDiagnosisCode;

    /**
     * 病理诊断名称
     */
    private String pathologyDiagnosisName;

    /**
     * 病理号
     */
    private String pathologyNumber;

    /**
     * 损伤、中毒外部原因编码
     */
    private String externalCauseOfInjuryOrPoisoningCode;

    /**
     * 损伤、中毒外部原因名称
     */
    private String externalCauseOfInjuryOrPoisoningName;

    /**
     * 有无药物过敏史
     */
    private String drugAllergyHistory;

    /**
     * 药物过敏名称
     */
    private String allergyDrugs;

    /**
     * HBsAg
     */
    private String nbsag;

    /**
     * HCV-Ab
     */
    private String hcvab;

    /**
     * HIV-Ab
     */
    private String hivab;

    /**
     * 科主任编码
     */
    private String departmentHeadCode;

    /**
     * 科主任
     */
    private String departmentHead;

    /**
     * 主（副主）任医师编码
     */
    private String attendingPhysicianCode;

    /**
     * 主（副主）任医师
     */
    private String attendingPhysician;

    /**
     * 主治医师编码
     */
    private String associatePhysicianCode;

    /**
     * 主治医师
     */
    private String associatePhysician;

    /**
     * 住院医师编码
     */
    private String residentPhysicianCode;

    /**
     * 住院医师
     */
    private String residentPhysician;

    /**
     * 责任护士编码
     */
    private String responsibleNurseCode;

    /**
     * 责任护士
     */
    private String responsibleNurse;

    /**
     * 进修医师
     */
    private String visitingPhysician;

    /**
     * 实习医师
     */
    private String internPhysician;

    /**
     * 编码员
     */
    private String coder;

    /**
     * 病案质量
     */
    private String medicalRecordQuality;

    /**
     * 质控医师
     */
    private String qualityControlPhysician;

    /**
     * 质控护师
     */
    private String qualityControlNurse;

    /**
     * 质控日期
     */
    private LocalDate qualityControlDate;

    /**
     * 死亡患者尸检
     */
    private String autopsyForDeceased;

    /**
     * ABO血型
     */
    private String aboBloodType;

    /**
     * Rh血型
     */
    private String rhBloodType;

    /**
     * 主要手术操作编码
     */
    private String pso1;

    /**
     * 主要手术操作名称
     */
    private String pso2;

    /**
     * 主要手术操作日期
     */
    private LocalDateTime pso3;

    /**
     * 主要手术操作级别
     */
    private String pso4;

    /**
     * 主要手术持续时间
     */
    private BigDecimal pso5;

    /**
     * 主要手术操作术者
     */
    private String pso6;

    /**
     * 主要手术操作Ⅰ助
     */
    private String pso7;

    /**
     * 主要手术操作Ⅱ助
     */
    private String pso8;

    /**
     * 主要手术操作切口愈合等级
     */
    private String pso9;

    /**
     * 主要手术操作麻醉方式
     */
    private String pso10;

    /**
     * 主要手术麻醉分级
     */
    private String pso11;

    /**
     * 主要手术操作麻醉医师
     */
    private String pso12;

    /**
     * 特级护理天数
     */
    private Integer intensiveCareDays;

    /**
     * 一级护理天数
     */
    private Integer firstLevelCareDays;

    /**
     * 二级护理天数
     */
    private Integer secondLevelCareDays;

    /**
     * 三级护理天数
     */
    private Integer thirdLevelCareDays;

    /**
     * 输血反应
     */
    private Integer bloodTransfusionReaction;

    /**
     * 红细胞
     */
    private BigDecimal redBloodCells;

    /**
     * 血小板
     */
    private BigDecimal platelets;

    /**
     * 血浆
     */
    private BigDecimal plasma;

    /**
     * 全血
     */
    private BigDecimal wholeBlood;

    /**
     * 自体血回输
     */
    private BigDecimal autologousBloodTransfusion;

    /**
     * 新生儿出生体重（克）1
     */
    private Integer a18x01;

    /**
     * 新生儿出生体重（克）2
     */
    private Integer a18x02;

    /**
     * 新生儿出生体重（克）3
     */
    private Integer a18x03;

    /**
     * 新生儿出生体重（克）4
     */
    private Integer a18x04;

    /**
     * 新生儿出生体重（克）5
     */
    private Integer a18x05;

    /**
     * 新生儿入院体重（克）
     */
    private Integer a17;

    /**
     * 颅脑损伤患者入院前昏迷时间（天）
     */
    private Integer c28;

    /**
     * 颅脑损伤患者入院前昏迷时间（小时）
     */
    private Integer c29;

    /**
     * 颅脑损伤患者入院前昏迷时间（分钟）
     */
    private Integer c30;

    /**
     * 颅脑损伤患者入院后昏迷时间（天）
     */
    private Integer c31;

    /**
     * 颅脑损伤患者入院后昏迷时间（小时）
     */
    private Integer c32;

    /**
     * 颅脑损伤患者入院后昏迷时间（分钟）
     */
    private Integer c33;

    /**
     * 有创呼吸机使用时间
     */
    private Integer c47;

    /**
     * 是否有出院31日内再住院计划
     */
    private String b36c;

    /**
     * 出院31天再住院计划目的
     */
    private String b37;

    /**
     * 离院方式
     */
    private String b34c;

    /**
     * 医嘱转院、转社区卫生服务机构/乡镇卫生院名称
     */
    private String b35;

    /**
     * 其他过敏史
     */
    private String otherAllergyHistory;

    /**
     * 其他过敏原
     */
    private String otherAllergens;

    /**
     * 挂号时间
     */
    private LocalDateTime registrationTime;

    /**
     * 报到时间
     */
    private LocalDateTime checkInTime;

    /**
     * 就诊时间
     */
    private LocalDateTime visitTime;

    /**
     * 接诊医师职称
     */
    private String physicianTitle;

    /**
     * 就诊类型
     */
    private String visitType;

    /**
     * 是否复诊
     */
    private String isRevisit;

    /**
     * 是否输液
     */
    private String isInfusion;

    /**
     * 是否为门诊慢特病患者
     */
    private String isSpecialPatient;

    /**
     * 急诊患者分级
     */
    private String emergencyLevel;

    /**
     * 急诊患者去向
     */
    private String emergencyOutcome;

    /**
     * 住院证开具时间
     */
    private LocalDateTime admissionCertTime;

    /**
     * 患者主诉
     */
    private String chiefComplaint;

    /**
     * 门（急）诊主要诊断
     */
    private String mainDiagnosis;

    /**
     * 门（急）诊其他诊断
     */
    private String otherDiagnosis;

    /**
     * 门（急）诊其他诊断编码
     */
    private String otherDiagnosisCode;

    /**
     * 手术及操作日期
     */
    private LocalDateTime surgeryDate;

    /**
     * 手术及操作名称
     */
    private String surgeryName;

    /**
     * 手术及操作编码
     */
    private String surgeryCode;

    /**
     * 手术及操作者
     */
    private String surgeon;

    /**
     * 麻醉方式
     */
    private String anesthesiaMethod;

    /**
     * 麻醉医师
     */
    private String anesthesiologist;

    /**
     * 手术分级管理级别
     */
    private Integer surgeryGrade;

    /**
     * 门（急）诊总费用
     */
    private BigDecimal totalCost;

    /**
     * 其中，自付金额
     */
    private BigDecimal selfPaidCost;

    /**
     * 1.一般医疗服务费
     */
    private BigDecimal generalServiceCost;

    /**
     * 2.一般治疗操作费
     */
    private BigDecimal generalTreatmentCost;

    /**
     * 3.护理费
     */
    private BigDecimal nursingCost;

    /**
     * 4.综合医疗服务类其他费用
     */
    private BigDecimal otherServiceCost;

    /**
     * 5.病理诊断费
     */
    private BigDecimal pathologyCost;

    /**
     * 6.实验室诊断费
     */
    private BigDecimal labTestCost;

    /**
     * 7.影像学诊断费
     */
    private BigDecimal imagingCost;

    /**
     * 8.临床诊断项目费
     */
    private BigDecimal clinicalCost;

    /**
     * 9.非手术治疗项目费
     */
    private BigDecimal nonSurgicalCost;

    /**
     * 其中：临床物理治疗费
     */
    private BigDecimal physicalTherapyCost;

    /**
     * 10.手术治疗费
     */
    private BigDecimal surgeryTotalCost;

    /**
     * 其中：麻醉费
     */
    private BigDecimal anesthesiaCost;

    /**
     * 其中：手术费
     */
    private BigDecimal surgeryCost;

    /**
     * 11.康复费
     */
    private BigDecimal recoveryCost;

    /**
     * 12.中医治疗费
     */
    private BigDecimal chineseMedicineCost;

    /**
     * 13.西药费
     */
    private BigDecimal westernMedicineCost;

    /**
     * 其中：抗菌药物费用
     */
    private BigDecimal antibioticCost;

    /**
     * 14.中成药费
     */
    private BigDecimal chinesePrepCost;

    /**
     * 15.中草药费
     */
    private BigDecimal herbalMedicineCost;

    /**
     * 16.血费
     */
    private BigDecimal bloodCost;

    /**
     * 17.白蛋白类制品费
     */
    private BigDecimal albuminCost;

    /**
     * 18.球蛋白类制品费
     */
    private BigDecimal globulinCost;

    /**
     * 19.凝血因子类制品费
     */
    private BigDecimal coagulationCost;

    /**
     * 20.细胞因子类制品费
     */
    private BigDecimal cytokineCost;

    /**
     * 21.检查用一次性医用材料费
     */
    private BigDecimal disposableExamCost;

    /**
     * 22.治疗用一次性医用材料费
     */
    private BigDecimal disposableTreatmentCost;

    /**
     * 23.手术用一次性医用材料费
     */
    private BigDecimal disposableSurgeryCost;

    /**
     * 24.其他费
     */
    private BigDecimal otherCost;

    /**
     * 职业
     */
    private String profession;

    /**
     * [A]入院诊断信息表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ArrayNode admissionDiagnosis;

    /**
     * [A]出院诊断信息表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ArrayNode dischargeDiagnosis;

    /**
     * [A]手术信息表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ArrayNode shoushuDetails;

    /**
     * [A]重症监护信息表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ArrayNode zhongzhengDetails;

    /**
     * 住院号
     */
    private String adNumber;

    /**
     * 页码
     */
    private String pageNumber;
}
