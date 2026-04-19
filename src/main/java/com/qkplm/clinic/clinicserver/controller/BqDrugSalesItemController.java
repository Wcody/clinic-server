/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月15日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qkplm.clinic.clinicserver.entity.BqDrugSalesItemEntity;
import com.qkplm.clinic.clinicserver.service.IBqDrugSalesItemService;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Wcke
 * @description <p>药房零售明细表 前端控制器</p>
 * @datetime 2026-4-15
 */
@RestController
@RequestMapping("/ams/api/v1/drug/sales/item")
public class BqDrugSalesItemController {

    private final static String MODULE_NAME = "药房零售明细表";
    private final static String TAG_NAME = "drugSalesItem";

    private final IBqDrugSalesItemService salesItemService;

    public BqDrugSalesItemController(IBqDrugSalesItemService salesItemService) {
        this.salesItemService = salesItemService;
    }

    /**
     * 根据零售主表ID查询明细列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "按销售单查询明细")
    @RequestMapping(value = "/bySalesId/{salesId}", method = RequestMethod.GET)
    public List<BqDrugSalesItemEntity> listBySalesId(@PathVariable Integer salesId) {
        return salesItemService.listBySalesId(salesId);
    }

    /**
     * 列表查询
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<BqDrugSalesItemEntity> list(BQSearchParamsGet<BqDrugSalesItemEntity> paramsGet) {
        BQSearchParams<BqDrugSalesItemEntity> params = paramsGet.toSearchParams();
        return salesItemService.list(params.toPage(), params.toWrapper());
    }

    /**
     * 分页查询
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BqDrugSalesItemEntity> page(BQSearchParamsGet<BqDrugSalesItemEntity> paramsGet) {
        BQSearchParams<BqDrugSalesItemEntity> params = paramsGet.toSearchParams();
        return salesItemService.page(params.toPage(), params.toWrapper());
    }

    /**
     * 逻辑删除单条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (!salesItemService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }
}
