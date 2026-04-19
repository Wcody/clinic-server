/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月18日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.dtos.BqPrescriptionFullDto;
import com.qkplm.clinic.clinicserver.dtos.BqSaveMedicalOrderDto;
import com.qkplm.clinic.clinicserver.service.IBqPrescriptionService;
import com.qkplm.clinic.clinicserver.service.impl.BqPrescriptionPrintService;

import java.util.List;
import com.qkplm.clinic.clinicserver.entity.BqPrescriptionEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>处方主表 前端控制器</p>
 * @datetime 2026-4-18 0:54
 */
@RestController
@RequestMapping("/ams/api/v1/prescription")
public class BqPrescriptionController {
    private final static String MODULE_NAME = "处方主表";
    private final static String TAG_NAME = "prescription";
    private final IBqPrescriptionService prescriptionService;
    private final BqPrescriptionPrintService prescriptionPrintService;

    public BqPrescriptionController(IBqPrescriptionService prescriptionService,
                                    BqPrescriptionPrintService prescriptionPrintService) {
        this.prescriptionService = prescriptionService;
        this.prescriptionPrintService = prescriptionPrintService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BqPrescriptionEntity get(@PathVariable String id) {
        BqPrescriptionEntity prescription = prescriptionService.getById(id);
        if (prescription == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return prescription;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BqPrescriptionEntity save(@RequestBody BqPrescriptionEntity prescription) {
        prescription.setId(null);
        if (!prescriptionService.save(prescription)) {
            throw new BQApiException("新增失败");
        }
        return prescription;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<BqPrescriptionEntity> prescriptions) {
        if (!prescriptionService.saveBatch(prescriptions)) {
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
    public BqPrescriptionEntity update(@RequestBody BqPrescriptionEntity prescription) {
        if (!prescriptionService.updateById(prescription)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return prescription;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<BqPrescriptionEntity> prescriptions) {
        if (!prescriptionService.updateBatchById(prescriptions)) {
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
        if (!prescriptionService.removePhysicalById(id)) {
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
        if (!prescriptionService.removePhysicalBatchByIds(ids)) {
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
        if (!prescriptionService.removeById(id)) {
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
        if (!prescriptionService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
     * 生成处方PDF，用于打印预览
     * GET /prescription/printPdf?regId=xxx&showPrice=true
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "打印处方")
    @RequestMapping(value = "/printPdf", method = RequestMethod.GET)
    public void printPdf(@RequestParam Integer regId,
                         @RequestParam(defaultValue = "true") Boolean showPrice,
                         HttpServletResponse response) throws Exception {
        byte[] pdfBytes = prescriptionPrintService.generatePdf(regId, showPrice);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=prescription.pdf");
        response.setContentLength(pdfBytes.length);
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }

    /**
     * 根据挂号ID查询所有处方及明细（一次性返回）
     * GET /prescription/listFullByRegId/{regId}
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "按挂号查询处方明细")
    @RequestMapping(value = "/listFullByRegId/{regId}", method = RequestMethod.GET)
    public List<BqPrescriptionFullDto> listFullByRegId(@PathVariable Integer regId) {
        return prescriptionService.listByRegId(regId);
    }

    /**
     * 统一事务保存医嘱（处方主表 + 处方明细）
     * 若患者/挂号不存在则自动新增
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "保存医嘱")
    @RequestMapping(value = "/saveMedicalOrder", method = RequestMethod.POST)
    public BqSaveMedicalOrderDto.Result saveMedicalOrder(@RequestBody BqSaveMedicalOrderDto dto) {
        return prescriptionService.saveMedicalOrder(dto);
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<BqPrescriptionEntity> list(BQSearchParamsGet<BqPrescriptionEntity> paramsGet) {
        BQSearchParams<BqPrescriptionEntity> params = paramsGet.toSearchParams();
        return prescriptionService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BqPrescriptionEntity> page(BQSearchParamsGet<BqPrescriptionEntity> paramsGet) {
        BQSearchParams<BqPrescriptionEntity> params = paramsGet.toSearchParams();
        return prescriptionService.page(params.toPage(), params.toWrapper());
    }
}
