/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.constant.buttons.BQParamItemButtons;
import com.qkplm.clinic.clinicserver.entity.ViewQcrEntity;
import com.qkplm.clinic.clinicserver.service.ITbRecordService;
import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;
import com.qkplm.clinic.clinicserver.service.IViewQcrService;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;
import com.qkplm.clinic.libcommon.utils.BQWrapperSqlUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Wcke
 * @description <p>病案表 前端控制器</p>
 * @datetime 2024-9-21 9:1
 */
@Slf4j
@RestController
@RequestMapping("/ams/api/v1/record")
public class TbRecordController {
    private final static String MODULE_NAME = "病案表";
    private final static String TAG_NAME = "record";
    private final ITbRecordService recordService;
    private final IViewQcrService qcrService;

    public TbRecordController(ITbRecordService recordService, IViewQcrService qcrService) {
        this.recordService = recordService;
        this.qcrService = qcrService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbRecordEntity get(@PathVariable String id) {
        TbRecordEntity record = recordService.getById(id);
        if (record == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return record;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbRecordEntity save(@RequestBody TbRecordEntity record) {
        record.setEid(null);
        if (!recordService.save(record)) {
            throw new BQApiException("新增失败");
        }
        return record;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbRecordEntity> records) {
        if (!recordService.saveBatch(records)) {
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
    public TbRecordEntity update(@RequestBody TbRecordEntity record) {
        if (!recordService.updateById(record)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return record;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbRecordEntity> records) {
        if (!recordService.updateBatchById(records)) {
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
        if (!recordService.removePhysicalById(id)) {
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
        if (!recordService.removePhysicalBatchByIds(ids)) {
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
        if (!recordService.removeById(id)) {
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
        if (!recordService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
     * Qcr列表查询，orders可以排序，filters可以过滤
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "Qcr列表查询")
    @RequestMapping(value = "/qcr/list", method = RequestMethod.GET)
    public Iterable<ViewQcrEntity> qcrList(BQSearchParamsGet<ViewQcrEntity> paramsGet) {
        BQSearchParams<ViewQcrEntity> params = paramsGet.toSearchParams();
        return qcrService.list(params.toPage(), params.toWrapper());
    }

    /**
     * Qcr分页查询，orders可以排序，filters可以过滤
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "Qcr分页查询")
    @RequestMapping(value = "/qcr/page", method = RequestMethod.GET)
    public Page<ViewQcrEntity> qcrPage(BQSearchParamsGet<ViewQcrEntity> paramsGet) {
        BQSearchParams<ViewQcrEntity> params = paramsGet.toSearchParams();
        return qcrService.page(params.toPage(), params.toWrapper());
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<TbRecordEntity> list(BQSearchParamsGet<TbRecordEntity> paramsGet) {
        BQSearchParams<TbRecordEntity> params = paramsGet.toSearchParams();
        return recordService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbRecordEntity> page(BQSearchParamsGet<TbRecordEntity> paramsGet) {
        BQSearchParams<TbRecordEntity> params = paramsGet.toSearchParams();
        QueryWrapper<TbRecordEntity> wrapper = params.toWrapper();
        log.info("getSqlSegment:{}", BQWrapperSqlUtils.getSqlFromWrapper(wrapper, TbRecordEntity.class));
        return recordService.page(params.toPage(), wrapper);
    }

    /**
     * 护士质控
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "护士质控")
    @RequestMapping(value = "/nurse/qc/{recordId}", method = RequestMethod.POST)
    public Boolean nurseQc(@PathVariable String recordId, @RequestBody String content) {
        return recordService.nurseQc(recordId, content);
    }

    /**
     * 医生质控
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "医生质控")
    @RequestMapping(value = "/doctor/qc/{recordId}", method = RequestMethod.POST)
    public Boolean doctorQc(@PathVariable String recordId, @RequestBody String content) {
        return recordService.doctorQc(recordId, content);
    }

    /**
     * 终末质控
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "终末质控")
    @RequestMapping(value = "/final/qc/{recordId}", method = RequestMethod.POST)
    public Boolean finalQc(@PathVariable String recordId, @RequestBody String content) {
        return recordService.finalQc(recordId, content);
    }

    /**
     * 医生质控退回
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "医生质控退回")
    @RequestMapping(value = "/doctor/qcb/{recordId}", method = RequestMethod.POST)
    public Boolean doctorQcb(@PathVariable String recordId, @RequestBody String content) {
        return recordService.doctorQcb(recordId, content);
    }

    /**
     * 终末质控退回
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "终末质控退回")
    @RequestMapping(value = "/final/qcb/{recordId}", method = RequestMethod.POST)
    public Boolean finalQcb(@PathVariable String recordId, @RequestBody String content) {
        return recordService.finalQcb(recordId, content);
    }

    /**
     * 获取所有字段字段Options
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQParamItemButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "获取所有字段字典")
    @RequestMapping(value = "/fieldOptions/{recordKind}", method = RequestMethod.GET)
    public Object fieldOptions(@PathVariable Integer recordKind) {
        return recordService.getFieldOptions(recordKind);
    }

    /**
     * 根据完整性规则病案列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "根据完整性规则病案列表")
    @RequestMapping(value = "/listWz", method = RequestMethod.POST)
    public Object listWz(@RequestBody HashMap<String, Object> params) {
        return recordService.listWz(params);
    }

    /**
     * 获取用户质控排行榜
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "获取用户质控排行榜")
    @RequestMapping(value = "/listRank", method = RequestMethod.POST)
    public Object listRank(@RequestBody HashMap<String, Object> params) {
        return recordService.listRank();
    }
}
