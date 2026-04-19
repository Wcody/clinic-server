/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月13日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qkplm.clinic.clinicserver.entity.BqRegistrationEntity;
import com.qkplm.clinic.clinicserver.mapper.BqRegistrationMapper;
import com.qkplm.clinic.clinicserver.service.IBqRegistrationService;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Wcke
 * @description
 *              <p>
 *              挂号列表(挂号记录)表 服务接口类
 *              </p>
 * @datetime 2026-4-13 11:55
 */
@Slf4j
@Service
public class BqRegistrationServiceImpl extends BaqiServiceImpl<BqRegistrationMapper, BqRegistrationEntity>
        implements IBqRegistrationService {

    @Override
    public Iterable<BqRegistrationEntity> searchByConditions(String patientName, LocalDateTime startTime,
            LocalDateTime endTime, String status) {
        // 构建查询条件
        LambdaQueryWrapper<BqRegistrationEntity> wrapper = new LambdaQueryWrapper<>();

        // 患者姓名模糊查询
        if (StringUtils.hasText(patientName)) {
            wrapper.like(BqRegistrationEntity::getPatient, patientName);
        }

        // 挂号时间范围查询（使用orderTime字段）
        if (startTime != null) {
            wrapper.ge(BqRegistrationEntity::getOrderTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(BqRegistrationEntity::getOrderTime, endTime);
        }

        // 挂号状态精确查询
        if (StringUtils.hasText(status)) {
            wrapper.eq(BqRegistrationEntity::getStatus, status);
        }

        // 过滤已删除的记录，并按创建时间倒序排列
        wrapper.eq(BqRegistrationEntity::getDeleted, false)
                .orderByDesc(BqRegistrationEntity::getCreatedTime);

        return this.list(wrapper);
    }

    @Override
    public int generateTodayRegistrationNo() {
        // 获取今天的开始和结束时间
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay(); // 今天 00:00:00
        LocalDateTime todayEnd = today.plusDays(1).atStartOfDay(); // 明天 00:00:00

        // 查询今天最大的挂号号码
        Integer maxRegNo = this.baseMapper.selectMaxRegistrationNoToday(todayStart, todayEnd);

        int nextNo = 1; // 默认从1开始

        if (!Objects.isNull(maxRegNo)) {
            try {
                nextNo = maxRegNo + 1;
                log.info("今天已有{}个挂号，下一个挂号号为: {}", maxRegNo, nextNo);
            } catch (NumberFormatException e) {
                log.warn("挂号号码格式异常: {}，将从1开始", maxRegNo);
                nextNo = 1;
            }
        } else {
            log.info("今天暂无挂号记录，第一个挂号号为: 1");
        }

        return nextNo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refundRegistration(String id) {
        // 1. 查询挂号记录
        BqRegistrationEntity registration = this.getById(id);
        if (registration == null) {
            log.error("退号失败：挂号记录不存在，ID: {}", id);
            throw new BQApiException("挂号记录不存在");
        }

        // 2. 校验状态：只有"待接诊"和"已接诊"可以退号
        String currentStatus = registration.getStatus();
        if (!"待接诊".equals(currentStatus) && !"已接诊".equals(currentStatus)) {
            log.error("退号失败：当前状态[{}]不允许退号，ID: {}", currentStatus, id);
            throw new BQApiException("当前状态[" + currentStatus + "]不允许退号");
        }

        // 3. 更新状态为"已退号"
        registration.setStatus("已退号");

        boolean result = this.updateById(registration);
        if (result) {
            log.info("退号成功，挂号ID: {}, 患者: {}", id, registration.getPatient());
        } else {
            log.error("退号失败，挂号ID: {}", id);
            throw new BQApiException("退号操作失败");
        }

        return result;
    }

    @Override
    public Map<String, Object> getDiaryList(
            String patientName,
            String doctor,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Integer currentPage,
            Integer pageSize) {

        long offset = (long) (currentPage - 1) * pageSize;
        List<Map<String, Object>> list = this.baseMapper.selectDiaryList(
                patientName, doctor, startTime, endTime, offset, pageSize);
        Long total = this.baseMapper.countDiaryList(
                patientName, doctor, startTime, endTime);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total == null ? 0 : total);
        result.put("currentPage", currentPage);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public Page<BqRegistrationEntity> getVisitRecordList(
            String patientName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String status,
            Integer currentPage,
            Integer pageSize) {

        // 构建分页对象
        Page<BqRegistrationEntity> page = new Page<>(currentPage, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<BqRegistrationEntity> wrapper = new LambdaQueryWrapper<>();

        // 患者姓名模糊查询
        if (StringUtils.hasText(patientName)) {
            wrapper.like(BqRegistrationEntity::getPatient, patientName);
        }

        // 挂号时间范围查询
        if (startTime != null) {
            wrapper.ge(BqRegistrationEntity::getOrderTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(BqRegistrationEntity::getOrderTime, endTime);
        }

        // 状态精确查询（必填）
        if (StringUtils.hasText(status)) {
            wrapper.eq(BqRegistrationEntity::getStatus, status);
        } else {
            log.warn("就诊列表查询时status参数为空");
            throw new BQApiException("状态参数不能为空");
        }

        // 过滤已删除的记录，并按创建时间倒序排列
        wrapper.eq(BqRegistrationEntity::getDeleted, false)
                .orderByDesc(BqRegistrationEntity::getCreatedTime);

        // 执行分页查询
        return this.page(page, wrapper);
    }

    @Override
    public Page<BqRegistrationEntity> getChargeList(
            String patientName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String statusFee,
            String status,
            Integer currentPage,
            Integer pageSize) {

        Page<BqRegistrationEntity> page = new Page<>(currentPage, pageSize);

        LambdaQueryWrapper<BqRegistrationEntity> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(patientName)) {
            wrapper.like(BqRegistrationEntity::getPatient, patientName);
        }
        if (startTime != null) {
            wrapper.ge(BqRegistrationEntity::getOrderTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(BqRegistrationEntity::getOrderTime, endTime);
        }
        if (StringUtils.hasText(statusFee)) {
            wrapper.eq(BqRegistrationEntity::getStatusFee, statusFee);
            wrapper.ne(BqRegistrationEntity::getStatus, "已退号");
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(BqRegistrationEntity::getStatus, status);
        }

        wrapper.eq(BqRegistrationEntity::getDeleted, false)
                .orderByDesc(BqRegistrationEntity::getCreatedTime);

        return this.page(page, wrapper);
    }

}
