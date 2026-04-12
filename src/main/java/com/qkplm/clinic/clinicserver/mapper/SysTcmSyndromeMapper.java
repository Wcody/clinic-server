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
* @description <p>中医证型相关查询映射器</p>
* @datetime 2026-4-11
*/
@Mapper
public interface SysTcmSyndromeMapper {

    /**
     * 根据疾病ID查询证型列表
     */
    @Select("SELECT id, diseaseId, syndromeName, syndromeFeature, treatmentMethod FROM sys_tcm_syndrome WHERE diseaseId = #{diseaseId} AND status = 1")
    List<Map<String, Object>> selectByDiseaseId(Long diseaseId);
}
