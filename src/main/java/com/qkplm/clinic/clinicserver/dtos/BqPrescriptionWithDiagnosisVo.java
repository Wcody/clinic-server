/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月22日
*/
package com.qkplm.clinic.clinicserver.dtos;

import com.qkplm.clinic.clinicserver.entity.BqPrescriptionEntity;
import lombok.Getter;
import lombok.Setter;

/**
* @author Wcke
* @description <p>处方主表扩展VO，包含关联的诊断信息</p>
* @datetime 2026-4-22
*/
@Getter
@Setter
public class BqPrescriptionWithDiagnosisVo extends BqPrescriptionEntity {

    /**
     * 诊断结果文本
     */
    private String diagnosis;

    /**
     * 诊断ID串（逗号分隔）
     */
    private String diagnosisIds;
}
