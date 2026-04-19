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
 * @description 工作量统计 Mapper（按医生聚合）
 */
@Mapper
public interface WorkloadStatsMapper {

    /**
     * 按医生聚合工作量统计列表
     */
    List<Map<String, Object>> selectWorkloadByDoctor(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("doctor") String doctor);

    /**
     * 汇总卡片：总销售额、总成本、总利润
     */
    Map<String, Object> selectWorkloadSummary(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("doctor") String doctor);
}
