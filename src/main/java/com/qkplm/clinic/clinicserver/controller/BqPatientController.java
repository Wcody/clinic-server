/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月13日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.service.IBqPatientService;
import com.qkplm.clinic.clinicserver.entity.BqPatientEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>患者信息表 前端控制器</p>
 * @datetime 2026-4-13 11:55
 */
@RestController
@RequestMapping("/ams/api/v1/patient")
public class BqPatientController {
    private final static String MODULE_NAME = "患者信息表";
    private final static String TAG_NAME = "patient";
    private final IBqPatientService patientService;

    public BqPatientController(IBqPatientService patientService) {
        this.patientService = patientService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BqPatientEntity get(@PathVariable String id) {
        BqPatientEntity patient = patientService.getById(id);
        if (patient == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return patient;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BqPatientEntity save(@RequestBody BqPatientEntity patient) {
        patient.setId(null);
        if (!patientService.save(patient)) {
            throw new BQApiException("新增失败");
        }
        return patient;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<BqPatientEntity> patients) {
        if (!patientService.saveBatch(patients)) {
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
    public BqPatientEntity update(@RequestBody BqPatientEntity patient) {
        if (!patientService.updateById(patient)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return patient;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<BqPatientEntity> patients) {
        if (!patientService.updateBatchById(patients)) {
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
        if (!patientService.removePhysicalById(id)) {
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
        if (!patientService.removePhysicalBatchByIds(ids)) {
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
        if (!patientService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
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
        if (!patientService.removeBatchByIds(ids)) {
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
    public Iterable<BqPatientEntity> list(BQSearchParamsGet<BqPatientEntity> paramsGet) {
        BQSearchParams<BqPatientEntity> params = paramsGet.toSearchParams();
        return patientService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BqPatientEntity> page(BQSearchParamsGet<BqPatientEntity> paramsGet) {
        BQSearchParams<BqPatientEntity> params = paramsGet.toSearchParams();
        return patientService.page(params.toPage(), params.toWrapper());
    }

    /**
    * 根据关键字查询患者列表（支持姓名、手机号、身份证号模糊查询）
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "关键字查询")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Iterable<BqPatientEntity> search(@RequestParam(required = false) String keyword) {
        return patientService.searchByKeyword(keyword);
    }
}
