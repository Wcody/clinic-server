/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.dtos.BQRecordDto;
import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
* @author Wcke
* @description <p>病案表 服务类</p>
* @datetime 2024-9-21 9:1
*/
public interface ITbRecordService extends IBaqiService<TbRecordEntity> {
    boolean nurseQc(String recordId, String content);
    boolean doctorQc(String recordId, String content);
    boolean finalQc(String recordId, String content);
    boolean doctorQcb(String recordId, String content);
    boolean finalQcb(String recordId, String content);
    // 根据病案号获取
    TbRecordEntity getByRecordCode(String recordCode);
    // 根据患者id和姓名获取
    TbRecordEntity getByPatientIdAndName(String patientId, String patientName);
    // 获取所有字段字典
    List<HashMap<String, String>> getFieldOptions(Integer recordKind);
    // 获取所有记录的完整性判断列表
    List<BQRecordDto> listWz(HashMap<String, Object>  params);
    // 获取今天用户质控排行榜
    List<HashMap<String, Object>> listRank();
    // 根据时间范围参数获取用户质控排行榜
    List<HashMap<String, Object>> listRank(LocalDateTime startTime, LocalDateTime endTime);
}
