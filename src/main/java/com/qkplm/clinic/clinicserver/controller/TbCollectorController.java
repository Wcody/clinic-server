/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.constant.buttons.BQFCMgrCMButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQFCMgrList1Buttons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQMCMgrCMButtons;
import com.qkplm.clinic.clinicserver.service.ITbCollectorService;
import com.qkplm.clinic.clinicserver.entity.TbCollectorEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>采集器 前端控制器</p>
 * @datetime 2025-1-2 22:26
 */
@RestController
@RequestMapping("/ams/api/v1/collector")
public class TbCollectorController {
    private final static String MODULE_NAME = "采集器";
    private final static String TAG_NAME = "collector";
    private final ITbCollectorService collectorService;

    public TbCollectorController(ITbCollectorService collectorService) {
        this.collectorService = collectorService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.SEARCH, BQFCMgrList1Buttons.SEARCH, BQMCMgrCMButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbCollectorEntity get(@PathVariable String id) {
        TbCollectorEntity collector = collectorService.getById(id);
        if (collector == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return collector;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.ADD, BQFCMgrList1Buttons.ADD, BQMCMgrCMButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbCollectorEntity save(@RequestBody TbCollectorEntity collector) {
        collector.setEid(null);
        if (!collectorService.save(collector)) {
            throw new BQApiException("新增失败");
        }
        return collector;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.ADD, BQFCMgrList1Buttons.ADD, BQMCMgrCMButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbCollectorEntity> collectors) {
        if (!collectorService.saveBatch(collectors)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
    * 根据ID更新单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.EDIT, BQFCMgrList1Buttons.EDIT, BQMCMgrCMButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public TbCollectorEntity update(@RequestBody TbCollectorEntity collector) {
        if (!collectorService.updateById(collector)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return collector;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.EDIT, BQFCMgrList1Buttons.EDIT, BQMCMgrCMButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbCollectorEntity> collectors) {
        if (!collectorService.updateBatchById(collectors)) {
            throw new BQApiException("批量更新失败");
        }
        return true;
    }


    /**
    * 根据ID删除单条记录，物理删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.DELETE, BQFCMgrList1Buttons.DELETE, BQMCMgrCMButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Boolean delete(@PathVariable String id) {
        if (!collectorService.removePhysicalById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录,物理删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.DELETE, BQFCMgrList1Buttons.DELETE, BQMCMgrCMButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public Boolean deleteBatch(@RequestBody Collection<Serializable> ids) {
        if (!collectorService.removePhysicalBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 根据ID删除单条记录，逻辑删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.DELETE, BQFCMgrList1Buttons.DELETE, BQMCMgrCMButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (!collectorService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录，逻辑删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.DELETE, BQFCMgrList1Buttons.DELETE, BQMCMgrCMButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteLogicBatch", method = RequestMethod.POST)
    public Boolean deleteLogicBatch(@RequestBody Collection<Serializable> ids) {
        if (!collectorService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.SEARCH, BQFCMgrList1Buttons.SEARCH, BQMCMgrCMButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<TbCollectorEntity> list(BQSearchParamsGet<TbCollectorEntity> paramsGet) {
        BQSearchParams<TbCollectorEntity> params = paramsGet.toSearchParams();
        return collectorService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.SEARCH, BQFCMgrList1Buttons.SEARCH, BQMCMgrCMButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbCollectorEntity> page(BQSearchParamsGet<TbCollectorEntity> paramsGet) {
        BQSearchParams<TbCollectorEntity> params = paramsGet.toSearchParams();
        return collectorService.page(params.toPage(), params.toWrapper());
    }

    /**
     * 设置状态
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.SET_STATUS, BQFCMgrList1Buttons.SET_STATUS, BQMCMgrCMButtons.SET_STATUS})
    @BQLogMark(module = MODULE_NAME, operation = "启用禁用")
    @RequestMapping(value = "/setStatus/{eid}/{status}", method = RequestMethod.POST)
    public Object setStatus(@PathVariable String eid, @PathVariable Boolean status) {
        return collectorService.setStatus(eid, status);
    }
}
