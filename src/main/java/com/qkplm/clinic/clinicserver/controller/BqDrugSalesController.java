/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.service.IBqDrugSalesService;
import com.qkplm.clinic.clinicserver.service.IBqDrugSalesItemService;
import com.qkplm.clinic.clinicserver.entity.BqDrugSalesEntity;
import com.qkplm.clinic.clinicserver.entity.BqDrugSalesItemEntity;
import com.qkplm.clinic.clinicserver.dtos.RetailSaveDto;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Wcke
 * @description <p>药房零售表 前端控制器</p>
 * @datetime 2026-4-14 12:56
 */
@RestController
@RequestMapping("/ams/api/v1/drug/sales")
public class BqDrugSalesController {
    private final static String MODULE_NAME = "药房零售表";
    private final static String TAG_NAME = "drugSales";
    private final IBqDrugSalesService drugSalesService;
    private final IBqDrugSalesItemService drugSalesItemService;

    public BqDrugSalesController(IBqDrugSalesService drugSalesService,
                                  IBqDrugSalesItemService drugSalesItemService) {
        this.drugSalesService = drugSalesService;
        this.drugSalesItemService = drugSalesItemService;
    }

    /**
     * 零售保存（主记录 + 明细，事务保证原子性）
     * status=false 为保存，status=true 为直接收费
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "零售保存")
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/retail/save", method = RequestMethod.POST)
    public BqDrugSalesEntity retailSave(@RequestBody RetailSaveDto dto) {
        if (dto == null || dto.getSales() == null) {
            throw new BQApiException("请求参数错误");
        }
        BqDrugSalesEntity sales = dto.getSales();
        sales.setId(null);
        if (!drugSalesService.save(sales)) {
            throw new BQApiException("零售记录保存失败");
        }
        List<BqDrugSalesItemEntity> items = dto.getItems();
        if (items != null && !items.isEmpty()) {
            items.forEach(item -> {
                item.setId(null);
                item.setSalesId(sales.getId());
            });
            if (!drugSalesItemService.saveBatch(items)) {
                throw new BQApiException("药品明细保存失败");
            }
        }
        return sales;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BqDrugSalesEntity get(@PathVariable String id) {
        BqDrugSalesEntity drugSales = drugSalesService.getById(id);
        if (drugSales == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return drugSales;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BqDrugSalesEntity save(@RequestBody BqDrugSalesEntity drugSales) {
        drugSales.setId(null);
        if (!drugSalesService.save(drugSales)) {
            throw new BQApiException("新增失败");
        }
        return drugSales;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<BqDrugSalesEntity> drugSaless) {
        if (!drugSalesService.saveBatch(drugSaless)) {
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
    public BqDrugSalesEntity update(@RequestBody BqDrugSalesEntity drugSales) {
        if (!drugSalesService.updateById(drugSales)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return drugSales;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<BqDrugSalesEntity> drugSaless) {
        if (!drugSalesService.updateBatchById(drugSaless)) {
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
        if (!drugSalesService.removePhysicalById(id)) {
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
        if (!drugSalesService.removePhysicalBatchByIds(ids)) {
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
        if (!drugSalesService.removeById(id)) {
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
        if (!drugSalesService.removeBatchByIds(ids)) {
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
    public Iterable<BqDrugSalesEntity> list(BQSearchParamsGet<BqDrugSalesEntity> paramsGet) {
        BQSearchParams<BqDrugSalesEntity> params = paramsGet.toSearchParams();
        return drugSalesService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BqDrugSalesEntity> page(BQSearchParamsGet<BqDrugSalesEntity> paramsGet) {
        BQSearchParams<BqDrugSalesEntity> params = paramsGet.toSearchParams();
        return drugSalesService.page(params.toPage(), params.toWrapper());
    }
}
