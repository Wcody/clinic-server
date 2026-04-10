/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.service.ITbRecordQualityControlService;
import com.qkplm.clinic.clinicserver.entity.TbRecordQualityControlEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>病案质控记录 前端控制器</p>
 * @datetime 2024-12-17 7:29
 */
@RestController
@RequestMapping("/ams/api/v1/record/quality/control")
public class TbRecordQualityControlController {
    private final static String MODULE_NAME = "病案质控记录";
    private final static String TAG_NAME = "recordQualityControl";
    private final ITbRecordQualityControlService recordQualityControlService;

    public TbRecordQualityControlController(ITbRecordQualityControlService recordQualityControlService) {
        this.recordQualityControlService = recordQualityControlService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbRecordQualityControlEntity get(@PathVariable String id) {
        TbRecordQualityControlEntity recordQualityControl = recordQualityControlService.getById(id);
        if (recordQualityControl == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return recordQualityControl;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbRecordQualityControlEntity save(@RequestBody TbRecordQualityControlEntity recordQualityControl) {
        recordQualityControl.setEid(null);
        if (!recordQualityControlService.save(recordQualityControl)) {
            throw new BQApiException("新增失败");
        }
        return recordQualityControl;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbRecordQualityControlEntity> recordQualityControls) {
        if (!recordQualityControlService.saveBatch(recordQualityControls)) {
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
    public TbRecordQualityControlEntity update(@RequestBody TbRecordQualityControlEntity recordQualityControl) {
        if (!recordQualityControlService.updateById(recordQualityControl)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return recordQualityControl;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbRecordQualityControlEntity> recordQualityControls) {
        if (!recordQualityControlService.updateBatchById(recordQualityControls)) {
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
        if (!recordQualityControlService.removePhysicalById(id)) {
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
        if (!recordQualityControlService.removePhysicalBatchByIds(ids)) {
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
        if (!recordQualityControlService.removeById(id)) {
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
        if (!recordQualityControlService.removeBatchByIds(ids)) {
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
    public Iterable<TbRecordQualityControlEntity> list(BQSearchParamsGet<TbRecordQualityControlEntity> paramsGet) {
        BQSearchParams<TbRecordQualityControlEntity> params = paramsGet.toSearchParams();
        return recordQualityControlService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbRecordQualityControlEntity> page(BQSearchParamsGet<TbRecordQualityControlEntity> paramsGet) {
        BQSearchParams<TbRecordQualityControlEntity> params = paramsGet.toSearchParams();
        return recordQualityControlService.page(params.toPage(), params.toWrapper());
    }
}
