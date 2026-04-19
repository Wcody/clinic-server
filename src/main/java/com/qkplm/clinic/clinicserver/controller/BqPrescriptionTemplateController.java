/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.service.IBqPrescriptionTemplateService;
import com.qkplm.clinic.clinicserver.entity.BqPrescriptionTemplateEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>处方模板主表 前端控制器</p>
 * @datetime 2026-4-14 12:56
 */
@RestController
@RequestMapping("/ams/api/v1/prescription/template")
public class BqPrescriptionTemplateController {
    private final static String MODULE_NAME = "处方模板主表";
    private final static String TAG_NAME = "prescriptionTemplate";
    private final IBqPrescriptionTemplateService prescriptionTemplateService;

    public BqPrescriptionTemplateController(IBqPrescriptionTemplateService prescriptionTemplateService) {
        this.prescriptionTemplateService = prescriptionTemplateService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BqPrescriptionTemplateEntity get(@PathVariable String id) {
        System.out.println("=== 查询处方模板详情 ===");
        System.out.println("请求ID: " + id);
        
        BqPrescriptionTemplateEntity prescriptionTemplate = prescriptionTemplateService.getById(id);
        
        if (prescriptionTemplate == null) {
            System.out.println("警告: 未找到ID为 " + id + " 的处方模板");
            throw new BQApiException("获取失败，对象可能不存在");
        }
        
        System.out.println("查询成功 - ID: " + prescriptionTemplate.getId() + ", 名称: " + prescriptionTemplate.getName());
        System.out.println("===============================");
        
        return prescriptionTemplate;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BqPrescriptionTemplateEntity save(@RequestBody BqPrescriptionTemplateEntity prescriptionTemplate) {
        prescriptionTemplate.setId(null);
        if (!prescriptionTemplateService.save(prescriptionTemplate)) {
            throw new BQApiException("新增失败");
        }
        return prescriptionTemplate;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<BqPrescriptionTemplateEntity> prescriptionTemplates) {
        if (!prescriptionTemplateService.saveBatch(prescriptionTemplates)) {
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
    public BqPrescriptionTemplateEntity update(@RequestBody BqPrescriptionTemplateEntity prescriptionTemplate) {
        if (!prescriptionTemplateService.updateById(prescriptionTemplate)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return prescriptionTemplate;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<BqPrescriptionTemplateEntity> prescriptionTemplates) {
        if (!prescriptionTemplateService.updateBatchById(prescriptionTemplates)) {
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
        if (!prescriptionTemplateService.removePhysicalById(id)) {
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
        if (!prescriptionTemplateService.removePhysicalBatchByIds(ids)) {
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
        if (!prescriptionTemplateService.removeById(id)) {
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
        if (!prescriptionTemplateService.removeBatchByIds(ids)) {
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
    public Iterable<BqPrescriptionTemplateEntity> list(BQSearchParamsGet<BqPrescriptionTemplateEntity> paramsGet) {
        BQSearchParams<BqPrescriptionTemplateEntity> params = paramsGet.toSearchParams();
        return prescriptionTemplateService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BqPrescriptionTemplateEntity> page(BQSearchParamsGet<BqPrescriptionTemplateEntity> paramsGet) {
        BQSearchParams<BqPrescriptionTemplateEntity> params = paramsGet.toSearchParams();
        return prescriptionTemplateService.page(params.toPage(), params.toWrapper());
    }

    /**
    * 获取树形结构数据
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "树形查询")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public java.util.List<java.util.Map<String, Object>> tree() {
        System.out.println("=== 处方模板树形接口被调用 ===");
        java.util.List<java.util.Map<String, Object>> result = prescriptionTemplateService.getTreeData();
        System.out.println("返回数据条数: " + (result == null ? 0 : result.size()));
        return result;
    }
}
