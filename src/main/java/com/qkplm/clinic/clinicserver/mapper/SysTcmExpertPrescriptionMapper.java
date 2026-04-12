/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月11日
*/

package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author Wcke
* @description <p>中医名医验方相关查询映射器</p>
* @datetime 2026-4-11
*/
@Mapper
public interface SysTcmExpertPrescriptionMapper {

    /**
     * 根据疾病ID查询名医验方列表
     */
    @Select("SELECT id, diseaseId, expertName, expertTitle as introduction, experienceTitle as prescriptionName, " +
            "experienceContent as composition, indication, majorFunction as analysis, modification FROM sys_tcm_expert_prescription " +
            "WHERE diseaseId = #{diseaseId} AND status = 1")
    List<Map<String, Object>> selectByDiseaseId(Long diseaseId);

    /**
     * 根据验方ID查询药物明细
     */
    @Select("SELECT epi.herbName, epi.dosage, epi.cookMethod as note FROM sys_tcm_expert_prescription_item epi " +
            "WHERE epi.expertPrescId = #{expertPrescId} ORDER BY epi.sort ASC")
    List<Map<String, Object>> selectItemsByExpertPrescId(Long expertPrescId);
}
