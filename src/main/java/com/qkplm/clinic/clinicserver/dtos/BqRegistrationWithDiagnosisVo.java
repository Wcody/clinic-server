/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月27日
*/
package com.qkplm.clinic.clinicserver.dtos;

import com.qkplm.clinic.clinicserver.entity.BqRegistrationEntity;
import lombok.Getter;
import lombok.Setter;

/**
* @author Wcke
* @description <p>挂号记录扩展VO，包含关联病历表的诊断信息</p>
* @datetime 2026-4-27
*/
@Getter
@Setter
public class BqRegistrationWithDiagnosisVo extends BqRegistrationEntity {

    /**
     * 诊断结果（关联 bq_medical_record.diagnosis）
     */
    private String diagnosis;
}
