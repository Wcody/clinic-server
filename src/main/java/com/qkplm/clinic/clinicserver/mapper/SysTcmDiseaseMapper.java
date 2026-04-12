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
* @description <p>中医疾病相关查询映射器</p>
* @datetime 2026-4-11
*/
@Mapper
public interface SysTcmDiseaseMapper {

    /**
     * 查询所有疾病(用于列表)
     */
    @Select("SELECT id, diseaseName, deptId as category, firstLetter as alphabet, diseasePinyin as pinyin, diseasePinyinInitial as pinyinInitial FROM sys_tcm_disease WHERE status = 1")
    List<Map<String, Object>> selectAllDiseases();

    /**
     * 根据ID查询疾病详情
     */
    @Select("SELECT id, diseaseName, deptId as category, firstLetter as alphabet, summary FROM sys_tcm_disease WHERE id = #{id} AND status = 1")
    Map<String, Object> selectById(Long id);
}
