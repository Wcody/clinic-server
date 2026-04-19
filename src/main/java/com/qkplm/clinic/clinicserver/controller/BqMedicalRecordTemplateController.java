/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.service.IBqMedicalRecordTemplateService;
import com.qkplm.clinic.clinicserver.entity.BqMedicalRecordTemplateEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>病历模板表 前端控制器</p>
 * @datetime 2026-4-14 12:56
 */
@RestController
@RequestMapping("/ams/api/v1/medical/record/template")
public class BqMedicalRecordTemplateController {
    private final static String MODULE_NAME = "病历模板表";
    private final static String TAG_NAME = "medicalRecordTemplate";
    private final IBqMedicalRecordTemplateService medicalRecordTemplateService;

    public BqMedicalRecordTemplateController(IBqMedicalRecordTemplateService medicalRecordTemplateService) {
        this.medicalRecordTemplateService = medicalRecordTemplateService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BqMedicalRecordTemplateEntity get(@PathVariable String id) {
        BqMedicalRecordTemplateEntity medicalRecordTemplate = medicalRecordTemplateService.getById(id);
        if (medicalRecordTemplate == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return medicalRecordTemplate;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BqMedicalRecordTemplateEntity save(@RequestBody BqMedicalRecordTemplateEntity medicalRecordTemplate) {
        medicalRecordTemplate.setId(null);
        if (!medicalRecordTemplateService.save(medicalRecordTemplate)) {
            throw new BQApiException("新增失败");
        }
        return medicalRecordTemplate;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<BqMedicalRecordTemplateEntity> medicalRecordTemplates) {
        if (!medicalRecordTemplateService.saveBatch(medicalRecordTemplates)) {
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
    public BqMedicalRecordTemplateEntity update(@RequestBody BqMedicalRecordTemplateEntity medicalRecordTemplate) {
        if (!medicalRecordTemplateService.updateById(medicalRecordTemplate)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return medicalRecordTemplate;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<BqMedicalRecordTemplateEntity> medicalRecordTemplates) {
        if (!medicalRecordTemplateService.updateBatchById(medicalRecordTemplates)) {
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
        if (!medicalRecordTemplateService.removePhysicalById(id)) {
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
        if (!medicalRecordTemplateService.removePhysicalBatchByIds(ids)) {
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
        if (!medicalRecordTemplateService.removeById(id)) {
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
        if (!medicalRecordTemplateService.removeBatchByIds(ids)) {
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
    public Iterable<BqMedicalRecordTemplateEntity> list(BQSearchParamsGet<BqMedicalRecordTemplateEntity> paramsGet) {
        BQSearchParams<BqMedicalRecordTemplateEntity> params = paramsGet.toSearchParams();
        return medicalRecordTemplateService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BqMedicalRecordTemplateEntity> page(BQSearchParamsGet<BqMedicalRecordTemplateEntity> paramsGet) {
        BQSearchParams<BqMedicalRecordTemplateEntity> params = paramsGet.toSearchParams();
        return medicalRecordTemplateService.page(params.toPage(), params.toWrapper());
    }

    /**
    * 获取树形结构数据
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "树形查询")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public java.util.List<java.util.Map<String, Object>> tree() {
        System.out.println("=== 病历模板树形接口被调用 ===");
        java.util.List<java.util.Map<String, Object>> result = medicalRecordTemplateService.getTreeData();
        System.out.println("返回数据条数: " + (result == null ? 0 : result.size()));
        return result;
    }
}
