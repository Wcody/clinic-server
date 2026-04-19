/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月18日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.service.IBqMedicalRecordService;
import com.qkplm.clinic.clinicserver.entity.BqMedicalRecordEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>门诊就诊记录表 前端控制器</p>
 * @datetime 2026-4-18 0:54
 */
@RestController
@RequestMapping("/ams/api/v1/medical/record")
public class BqMedicalRecordController {
    private final static String MODULE_NAME = "门诊就诊记录表";
    private final static String TAG_NAME = "medicalRecord";
    private final IBqMedicalRecordService medicalRecordService;

    public BqMedicalRecordController(IBqMedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BqMedicalRecordEntity get(@PathVariable String id) {
        BqMedicalRecordEntity medicalRecord = medicalRecordService.getById(id);
        if (medicalRecord == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return medicalRecord;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BqMedicalRecordEntity save(@RequestBody BqMedicalRecordEntity medicalRecord) {
        medicalRecord.setId(null);
        if (!medicalRecordService.save(medicalRecord)) {
            throw new BQApiException("新增失败");
        }
        return medicalRecord;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<BqMedicalRecordEntity> medicalRecords) {
        if (!medicalRecordService.saveBatch(medicalRecords)) {
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
    public BqMedicalRecordEntity update(@RequestBody BqMedicalRecordEntity medicalRecord) {
        if (!medicalRecordService.updateById(medicalRecord)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return medicalRecord;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<BqMedicalRecordEntity> medicalRecords) {
        if (!medicalRecordService.updateBatchById(medicalRecords)) {
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
        if (!medicalRecordService.removePhysicalById(id)) {
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
        if (!medicalRecordService.removePhysicalBatchByIds(ids)) {
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
        if (!medicalRecordService.removeById(id)) {
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
        if (!medicalRecordService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
     * 根据挂号ID查询病历
     * GET /medical/record/getByRegId/{regId}
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "按挂号查询病历")
    @RequestMapping(value = "/getByRegId/{regId}", method = RequestMethod.GET)
    public BqMedicalRecordEntity getByRegId(@PathVariable Integer regId) {
        return medicalRecordService.getByRegId(regId);
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<BqMedicalRecordEntity> list(BQSearchParamsGet<BqMedicalRecordEntity> paramsGet) {
        BQSearchParams<BqMedicalRecordEntity> params = paramsGet.toSearchParams();
        return medicalRecordService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BqMedicalRecordEntity> page(BQSearchParamsGet<BqMedicalRecordEntity> paramsGet) {
        BQSearchParams<BqMedicalRecordEntity> params = paramsGet.toSearchParams();
        return medicalRecordService.page(params.toPage(), params.toWrapper());
    }
}
