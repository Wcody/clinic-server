/*
* 版权声明 Copyright (c) 2026。
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
 * @description 首页Dashboard统计 Mapper
 */
@Mapper
public interface HomeDashboardMapper {

    /**
     * 今日挂号总量
     */
    Integer selectTodayRegistrationTotal(@Param("dayStart") LocalDateTime dayStart, @Param("dayEnd") LocalDateTime dayEnd);

    /**
     * 诊所已接诊数量
     */
    Integer selectReceivedCount(@Param("dayStart") LocalDateTime dayStart, @Param("dayEnd") LocalDateTime dayEnd);

    /**
     * 诊所待接诊数量
     */
    Integer selectPendingCount(@Param("dayStart") LocalDateTime dayStart, @Param("dayEnd") LocalDateTime dayEnd);

    /**
     * 诊所今日收费总额（从挂号表统计totalPrice）
     */
    java.math.BigDecimal selectTodayFee(@Param("dayStart") LocalDateTime dayStart, @Param("dayEnd") LocalDateTime dayEnd);

    /**
     * 我的已接诊数量
     */
    Integer selectMyReceivedCount(@Param("doctor") String doctor, @Param("dayStart") LocalDateTime dayStart, @Param("dayEnd") LocalDateTime dayEnd);

    /**
     * 我的待接诊数量
     */
    Integer selectMyPendingCount(@Param("doctor") String doctor, @Param("dayStart") LocalDateTime dayStart, @Param("dayEnd") LocalDateTime dayEnd);

    /**
     * 我的今日收费总额
     */
    java.math.BigDecimal selectMyTodayFee(@Param("doctor") String doctor, @Param("dayStart") LocalDateTime dayStart, @Param("dayEnd") LocalDateTime dayEnd);

    /**
     * 本周就诊趋势（按星期分组）
     */
    List<Map<String, Object>> selectThisWeekVisitCount(@Param("weekStart") LocalDateTime weekStart, @Param("weekEnd") LocalDateTime weekEnd);

    /**
     * 上周就诊趋势（按星期分组）
     */
    List<Map<String, Object>> selectLastWeekVisitCount(@Param("weekStart") LocalDateTime weekStart, @Param("weekEnd") LocalDateTime weekEnd);

    /**
     * 今日接诊数量排行（按医生分组）
     */
    List<Map<String, Object>> selectVisitRank(@Param("dayStart") LocalDateTime dayStart, @Param("dayEnd") LocalDateTime dayEnd, @Param("limit") int limit);

    /**
     * 今日收费金额排行（按医生分组）
     */
    List<Map<String, Object>> selectFeeRank(@Param("dayStart") LocalDateTime dayStart, @Param("dayEnd") LocalDateTime dayEnd, @Param("limit") int limit);

    /**
     * 最新动态（最近的就诊记录）
     */
    List<Map<String, Object>> selectRecentEvents(@Param("startTime") LocalDateTime startTime, @Param("limit") int limit);
}
