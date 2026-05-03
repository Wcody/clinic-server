/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月13日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import com.qkplm.clinic.clinicserver.service.IBqRegistrationService;
import com.qkplm.clinic.clinicserver.dtos.BqRegistrationWithDiagnosisVo;
import com.qkplm.clinic.clinicserver.entity.BqRegistrationEntity;
import com.qkplm.clinic.clinicserver.dtos.RegistrationSaveDto;
import com.qkplm.clinic.clinicserver.mapper.SysRegionMapper;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Wcke
 * @description
 *              <p>
 *              挂号列表(挂号记录)表 前端控制器
 *              </p>
 * @datetime 2026-4-13 11:55
 */
@RestController
@RequestMapping("/ams/api/v1/registration")
public class BqRegistrationController {
    private final static String MODULE_NAME = "挂号列表(挂号记录)表";
    private final static String TAG_NAME = "registration";
    private final IBqRegistrationService registrationService;
    private final SysRegionMapper regionMapper;

    public BqRegistrationController(IBqRegistrationService registrationService, SysRegionMapper regionMapper) {
        this.registrationService = registrationService;
        this.regionMapper = regionMapper;
    }

    /**
     * 根据ID获取单条记录
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BqRegistrationEntity get(@PathVariable String id) {
        BqRegistrationEntity registration = registrationService.getById(id);
        if (registration == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return registration;
    }

    /**
     * 新增挂号记录（同时处理患者信息）
     * 
     * @param saveDto 包含挂号信息和患者信息的DTO
     * @return 挂号记录
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BqRegistrationEntity save(@RequestBody RegistrationSaveDto saveDto) {
        if (saveDto == null || saveDto.getRegistration() == null) {
            throw new BQApiException("请求参数错误");
        }

        BqRegistrationEntity registration = saveDto.getRegistration();

        // 保存挂号记录
        registration.setId(null);
        registration.setOrderTime(LocalDateTime.now());

        // 生成挂号单号
        registration.setRegistrationNo(registrationService.generateTodayRegistrationNo());

        if (!registrationService.save(registration)) {
            throw new BQApiException("挂号记录新增失败");
        }

        return registration;
    }

    /**
     * 批量新增记录，默认单次执行1000条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<BqRegistrationEntity> registrations) {
        if (!registrationService.saveBatch(registrations)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
     * 根据ID更新单条记录
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BqRegistrationEntity update(@RequestBody BqRegistrationEntity registration) {
        if (!registrationService.updateById(registration)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return registration;
    }

    /**
     * 批量更新记录，默认单次执行1000条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<BqRegistrationEntity> registrations) {
        if (!registrationService.updateBatchById(registrations)) {
            throw new BQApiException("批量更新失败");
        }
        return true;
    }

    /**
     * 根据ID删除单条记录，物理删除
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Boolean delete(@PathVariable String id) {
        if (!registrationService.removePhysicalById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
     * 批量删除记录,物理删除，默认单次执行1000条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public Boolean deleteBatch(@RequestBody Collection<Serializable> ids) {
        if (!registrationService.removePhysicalBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
     * 根据ID删除单条记录，逻辑删除
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (!registrationService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
     * 退号（更新状态为"已退号"）
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "退号")
    @RequestMapping(value = "/refund/{id}", method = RequestMethod.GET)
    public Boolean refund(@PathVariable String id) {
        if (!registrationService.refundRegistration(id)) {
            throw new BQApiException("退号失败");
        }
        return true;
    }

    /**
     * 批量删除记录，逻辑删除，默认单次执行1000条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteLogicBatch", method = RequestMethod.POST)
    public Boolean deleteLogicBatch(@RequestBody Collection<Serializable> ids) {
        if (!registrationService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
     * 列表查询，orders可以排序，filters可以过滤
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<BqRegistrationEntity> list(BQSearchParamsGet<BqRegistrationEntity> paramsGet) {
        BQSearchParams<BqRegistrationEntity> params = paramsGet.toSearchParams();
        return registrationService.list(params.toPage(), params.toWrapper());
    }

    /**
     * 分页查询，orders可以排序，filters可以过滤
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BqRegistrationEntity> page(BQSearchParamsGet<BqRegistrationEntity> paramsGet) {
        BQSearchParams<BqRegistrationEntity> params = paramsGet.toSearchParams();
        return registrationService.page(params.toPage(), params.toWrapper());
    }

    /**
     * 根据患者姓名、挂号时间范围、挂号状态查询挂号列表
     * 
     * @param patientName 患者姓名（可选，模糊查询）
     * @param startTime   开始时间（可选）
     * @param endTime     结束时间（可选）
     * @param status      挂号状态（可选）
     * @return 挂号列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "条件查询")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Iterable<BqRegistrationEntity> search(
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String status) {
        return registrationService.searchByConditions(patientName, startTime, endTime, status);
    }

    /**
     * 获取省份列表
     * 
     * @return 省份列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询省份")
    @RequestMapping(value = "/provinces", method = RequestMethod.GET)
    public List<Map<String, Object>> getProvinces() {
        return regionMapper.selectAllProvinces();
    }

    /**
     * 根据省份ID获取城市列表
     * 
     * @param provinceId 省份ID
     * @return 城市列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询城市")
    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public List<Map<String, Object>> getCities(@RequestParam Integer provinceId) {
        if (provinceId == null) {
            return Collections.emptyList();
        }
        return regionMapper.selectCitiesByProvinceId(provinceId);
    }

    /**
     * 根据城市ID获取区县/街道列表
     * 
     * @param cityId 城市ID
     * @return 区县/街道列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询区县")
    @RequestMapping(value = "/districts", method = RequestMethod.GET)
    public List<Map<String, Object>> getDistricts(@RequestParam Integer cityId) {
        if (cityId == null) {
            return Collections.emptyList();
        }
        return regionMapper.selectDistrictsByCityId(cityId);
    }

    /**
     * 药房收费列表分页查询
     * 待收费：statusFee=未缴费 且 status!=已退号
     * 已诊患者：status=已接诊
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "收费列表查询")
    @RequestMapping(value = "/charge/list", method = RequestMethod.GET)
    public Map<String, Object> getChargeList(
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String statusFee,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {

        Page<BqRegistrationWithDiagnosisVo> page = registrationService.getChargeList(
                patientName, startTime, endTime, statusFee, status, currentPage, pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("list", page.getRecords());
        result.put("total", page.getTotal());
        result.put("currentPage", page.getCurrent());
        result.put("pageSize", page.getSize());
        return result;
    }

    /**
     * 门诊日志分页查询（关联患者信息）
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "门诊日志查询")
    @RequestMapping(value = "/diary/list", method = RequestMethod.GET)
    public Map<String, Object> getDiaryList(
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String doctor,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return registrationService.getDiaryList(patientName, doctor, startTime, endTime, currentPage, pageSize);
    }

    /**
     * 查询就诊记录列表（基于挂号记录）
     * 根据状态筛选：待接诊/已接诊
     * 
     * @param patientName 患者姓名（可选，模糊查询）
     * @param startTime   开始时间（可选）
     * @param endTime     结束时间（可选）
     * @param status      状态（必填）：待接诊/已接诊
     * @param currentPage 当前页（可选，默认1）
     * @param pageSize    每页大小（可选，默认20）
     * @return 分页的就诊记录列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "就诊列表查询")
    @RequestMapping(value = "/visit/list", method = RequestMethod.GET)
    public Map<String, Object> getVisitRecordList(
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = true) String status,
            @RequestParam(required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {

        if (Objects.isNull(startTime)) {
            // 将 startTime 默认为今天 00:00:00
            startTime = LocalDate.now().atStartOfDay();
        }

        if (Objects.isNull(endTime)) {
            // 将 endTime 默认为明天 00:00:00
            endTime = LocalDate.now().plusDays(1).atStartOfDay();
        }

        // 调用Service层查询方法
        Page<BqRegistrationWithDiagnosisVo> page = registrationService.getVisitRecordList(
                patientName, startTime, endTime, status, currentPage, pageSize);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", page.getRecords());
        result.put("total", page.getTotal());
        result.put("currentPage", page.getCurrent());
        result.put("pageSize", page.getSize());

        return result;
    }
}
