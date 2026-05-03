/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.service.IBqTreatmentItemService;
import com.qkplm.clinic.clinicserver.entity.BqTreatmentItemEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>治疗项目表 前端控制器</p>
 * @datetime 2026-4-14 12:56
 */
@RestController
@RequestMapping("/ams/api/v1/treatment/item")
public class BqTreatmentItemController {
    private final static String MODULE_NAME = "治疗项目表";
    private final static String TAG_NAME = "treatmentItem";
    private final IBqTreatmentItemService treatmentItemService;

    public BqTreatmentItemController(IBqTreatmentItemService treatmentItemService) {
        this.treatmentItemService = treatmentItemService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BqTreatmentItemEntity get(@PathVariable String id) {
        BqTreatmentItemEntity treatmentItem = treatmentItemService.getById(id);
        if (treatmentItem == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return treatmentItem;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BqTreatmentItemEntity save(@RequestBody BqTreatmentItemEntity treatmentItem) {
        treatmentItem.setId(null);
        if (!treatmentItemService.save(treatmentItem)) {
            throw new BQApiException("新增失败");
        }
        return treatmentItem;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<BqTreatmentItemEntity> treatmentItems) {
        if (!treatmentItemService.saveBatch(treatmentItems)) {
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
    public BqTreatmentItemEntity update(@RequestBody BqTreatmentItemEntity treatmentItem) {
        if (!treatmentItemService.updateById(treatmentItem)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return treatmentItem;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<BqTreatmentItemEntity> treatmentItems) {
        if (!treatmentItemService.updateBatchById(treatmentItems)) {
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
        if (!treatmentItemService.removePhysicalById(id)) {
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
        if (!treatmentItemService.removePhysicalBatchByIds(ids)) {
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
        if (!treatmentItemService.removeById(id)) {
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
        if (!treatmentItemService.removeBatchByIds(ids)) {
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
    public Iterable<BqTreatmentItemEntity> list(BQSearchParamsGet<BqTreatmentItemEntity> paramsGet) {
        BQSearchParams<BqTreatmentItemEntity> params = paramsGet.toSearchParams();
        return treatmentItemService.list(params.toPage(), params.toWrapper());
    }

    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "关键字搜索")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Iterable<BqTreatmentItemEntity> search(@RequestParam(required = false) String keyword) {
        var query = treatmentItemService.lambdaQuery()
                .orderByAsc(BqTreatmentItemEntity::getSeq);

        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim();
            query.and(wrapper -> wrapper
                    .like(BqTreatmentItemEntity::getName, kw)
                    .or()
                    .like(BqTreatmentItemEntity::getPinyin, kw));
        }

        return query.list();
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BqTreatmentItemEntity> page(BQSearchParamsGet<BqTreatmentItemEntity> paramsGet) {
        BQSearchParams<BqTreatmentItemEntity> params = paramsGet.toSearchParams();
        return treatmentItemService.page(params.toPage(), params.toWrapper());
    }
}
