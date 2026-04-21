/*
* 版权声明 Copyright (c) 2026.
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月20日
*/
package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Wcke
 * @description 药品销售统计 Mapper
 */
@Mapper
public interface DrugSalesStatsMapper {

    /**
     * 药品销售明细列表（分页）
     */
    List<Map<String, Object>> selectDrugSalesDetail(
            @Param("drugName") String drugName,
            @Param("doctor") String doctor,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("offset") long offset,
            @Param("limit") int limit);

    Long countDrugSalesDetail(
            @Param("drugName") String drugName,
            @Param("doctor") String doctor,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 药品销售统计（按药品聚合）
     */
    List<Map<String, Object>> selectDrugSalesStat(
            @Param("drugName") String drugName,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("offset") long offset,
            @Param("limit") int limit);

    Long countDrugSalesStat(
            @Param("drugName") String drugName,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 药品销售统计汇总
     */
    Map<String, Object> selectDrugSalesStatSummary(
            @Param("drugName") String drugName,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}