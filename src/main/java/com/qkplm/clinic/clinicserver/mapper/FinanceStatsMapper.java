/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/
package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Wcke
 * @description 财务统计 Mapper
 */
@Mapper
public interface FinanceStatsMapper {

    /**
     * 按天聚合财务统计列表
     */
    List<Map<String, Object>> selectStatsByDay(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("typeString") String typeString,
            @Param("offset") long offset,
            @Param("limit") int limit);

    Long countStatsByDay(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("typeString") String typeString);

    /**
     * 按月聚合财务统计列表
     */
    List<Map<String, Object>> selectStatsByMonth(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("typeString") String typeString,
            @Param("offset") long offset,
            @Param("limit") int limit);

    Long countStatsByMonth(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("typeString") String typeString);

    /**
     * 汇总卡片数据（累积金额、实收金额、退费金额）
     */
    Map<String, Object> selectSummary(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("typeString") String typeString);

    /**
     * 折线图数据（按天）
     */
    List<Map<String, Object>> selectChartByDay(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("typeString") String typeString);

    /**
     * 折线图数据（按月）
     */
    List<Map<String, Object>> selectChartByMonth(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("typeString") String typeString);

    /**
     * 查询所有收费类型（下拉选项）
     */
    List<String> selectTypeStrings();
}
