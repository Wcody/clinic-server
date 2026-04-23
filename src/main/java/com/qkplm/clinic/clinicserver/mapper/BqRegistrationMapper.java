/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月13日
*/

package com.qkplm.clinic.clinicserver.mapper;

import com.qkplm.clinic.clinicserver.entity.BqRegistrationEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Wcke
* @description <p>挂号列表(挂号记录)表 映射器</p>
* @datetime 2026-4-13 11:55
*/
@Mapper
public interface BqRegistrationMapper extends IBaqiMapper<BqRegistrationEntity> {

    /**
     * 查询今天最大的挂号号码
     * @param todayStart 今天的开始时间
     * @param todayEnd 今天的结束时间
     * @return 最大挂号号码，如果没有则返回null
     */
    Integer selectMaxRegistrationNoToday(LocalDateTime todayStart, LocalDateTime todayEnd);

    /**
     * 门诊日志分页查询（关联患者信息）
     */
    List<Map<String, Object>> selectDiaryList(
            @Param("patientName") String patientName,
            @Param("doctor") String doctor,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("offset") long offset,
            @Param("limit") int limit);

    /**
     * 门诊日志总数查询
     */
    Long countDiaryList(
            @Param("patientName") String patientName,
            @Param("doctor") String doctor,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 就诊记录分页查询（关联患者表获取冗余字段）
     */
    List<BqRegistrationEntity> selectVisitRecordList(
            @Param("patientName") String patientName,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") String status,
            @Param("offset") long offset,
            @Param("limit") int limit);

    /**
     * 就诊记录总数查询
     */
    Long countVisitRecordList(
            @Param("patientName") String patientName,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") String status);
}
