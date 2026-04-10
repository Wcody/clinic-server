/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月9日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.service.ITbDiagnosisDictService;
import com.qkplm.clinic.clinicserver.entity.TbDiagnosisDictEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>诊断字典表 前端控制器</p>
 * @datetime 2026-4-9 11:43
 */
@RestController
@RequestMapping("/ams/api/v1/diagnosis/dict")
public class TbDiagnosisDictController {
    private final static String MODULE_NAME = "诊断字典表";
    private final static String TAG_NAME = "diagnosisDict";
    private final ITbDiagnosisDictService diagnosisDictService;

    public TbDiagnosisDictController(ITbDiagnosisDictService diagnosisDictService) {
        this.diagnosisDictService = diagnosisDictService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbDiagnosisDictEntity get(@PathVariable String id) {
        TbDiagnosisDictEntity diagnosisDict = diagnosisDictService.getById(id);
        if (diagnosisDict == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return diagnosisDict;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbDiagnosisDictEntity save(@RequestBody TbDiagnosisDictEntity diagnosisDict) {
        diagnosisDict.setEid(null);
        if (!diagnosisDictService.save(diagnosisDict)) {
            throw new BQApiException("新增失败");
        }
        return diagnosisDict;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbDiagnosisDictEntity> diagnosisDicts) {
        if (!diagnosisDictService.saveBatch(diagnosisDicts)) {
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
    public TbDiagnosisDictEntity update(@RequestBody TbDiagnosisDictEntity diagnosisDict) {
        if (!diagnosisDictService.updateById(diagnosisDict)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return diagnosisDict;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbDiagnosisDictEntity> diagnosisDicts) {
        if (!diagnosisDictService.updateBatchById(diagnosisDicts)) {
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
        if (!diagnosisDictService.removePhysicalById(id)) {
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
        if (!diagnosisDictService.removePhysicalBatchByIds(ids)) {
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
        if (!diagnosisDictService.removeById(id)) {
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
        if (!diagnosisDictService.removeBatchByIds(ids)) {
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
    public Iterable<TbDiagnosisDictEntity> list(BQSearchParamsGet<TbDiagnosisDictEntity> paramsGet) {
        BQSearchParams<TbDiagnosisDictEntity> params = paramsGet.toSearchParams();
        return diagnosisDictService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbDiagnosisDictEntity> page(BQSearchParamsGet<TbDiagnosisDictEntity> paramsGet) {
        BQSearchParams<TbDiagnosisDictEntity> params = paramsGet.toSearchParams();
        return diagnosisDictService.page(params.toPage(), params.toWrapper());
    }
}
