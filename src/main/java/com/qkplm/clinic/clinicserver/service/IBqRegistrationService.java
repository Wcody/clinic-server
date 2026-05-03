/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月13日
*/

package com.qkplm.clinic.clinicserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qkplm.clinic.clinicserver.dtos.BqRegistrationWithDiagnosisVo;
import com.qkplm.clinic.clinicserver.entity.BqRegistrationEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Wcke
 * @description
 *              <p>
 *              挂号列表(挂号记录)表 服务类
 *              </p>
 * @datetime 2026-4-13 11:55
 */
public interface IBqRegistrationService extends IBaqiService<BqRegistrationEntity> {

    /**
     * 根据患者姓名、挂号时间范围、挂号状态查询挂号列表
     * 
     * @param patientName 患者姓名（可选，模糊查询）
     * @param startTime   开始时间（可选）
     * @param endTime     结束时间（可选）
     * @param status      挂号状态（可选）
     * @return 挂号列表
     */
    Iterable<BqRegistrationEntity> searchByConditions(String patientName, LocalDateTime startTime,
            LocalDateTime endTime, String status);

    /**
     * 生成今天的挂号号码
     * 规则：查询今天已挂号的最大号码+1，如果没有则从1开始
     * 
     * @return 挂号号码字符串
     */
    int generateTodayRegistrationNo();

    /**
     * 退号（更新状态为"已退号"）
     * 
     * @param id 挂号记录ID
     * @return 是否成功
     */
    boolean refundRegistration(String id);

    /**
     * 查询就诊记录列表（基于挂号记录）
     * 
     * @param patientName 患者姓名（可选，模糊查询）
     * @param startTime   开始时间（可选）
     * @param endTime     结束时间（可选）
     * @param status      状态（必填）：待接诊/已接诊
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 分页结果
     */
    Page<BqRegistrationWithDiagnosisVo> getVisitRecordList(
            String patientName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String status,
            Integer currentPage,
            Integer pageSize);

    /**
     * 药房收费列表分页查询
     * 待收费：statusFee=未缴费 且 status!=已退号
     * 已诊患者：status=已接诊
     *
     * @param patientName 患者姓名（可选，模糊查询）
     * @param startTime   开始时间（可选）
     * @param endTime     结束时间（可选）
     * @param statusFee   收费状态（可选）：未缴费/已缴费
     * @param status      就诊状态（可选）：已接诊
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 分页结果
     */
    Page<BqRegistrationWithDiagnosisVo> getChargeList(
            String patientName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String statusFee,
            String status,
            Integer currentPage,
            Integer pageSize);

    /**
     * 门诊日志分页查询（关联患者信息）
     * 
     * @param patientName 患者姓名（模糊）
     * @param doctor      医生（精确）
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 包含 list/total 的 Map
     */
    Map<String, Object> getDiaryList(
            String patientName,
            String doctor,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Integer currentPage,
            Integer pageSize);

}
