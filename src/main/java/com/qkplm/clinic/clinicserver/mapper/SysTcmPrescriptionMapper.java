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
* @description <p>中医方剂相关查询映射器</p>
* @datetime 2026-4-11
*/
@Mapper
public interface SysTcmPrescriptionMapper {

    /**
     * 根据证型ID查询方剂列表
     */
    @Select("SELECT p.id, p.syndromeId, p.basicPrescId, bp.prescName, p.adjustment FROM sys_tcm_prescription p " +
            "LEFT JOIN sys_tcm_basic_prescription bp ON p.basicPrescId = bp.id " +
            "WHERE p.syndromeId = #{syndromeId} AND p.status = 1")
    List<Map<String, Object>> selectBySyndromeId(Long syndromeId);

    /**
     * 根据方剂ID查询药物组成
     */
    @Select("SELECT bpi.herbName, bpi.dosage, bpi.cookMethod as note FROM sys_tcm_basic_prescription_item bpi " +
            "WHERE bpi.basicPrescId = #{basicPrescId} ORDER BY bpi.sort ASC")
    List<Map<String, Object>> selectIngredientsByPrescId(Long basicPrescId);
}
