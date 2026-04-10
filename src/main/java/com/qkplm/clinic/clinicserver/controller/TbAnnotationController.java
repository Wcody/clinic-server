/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.service.ITbAnnotationService;
import com.qkplm.clinic.clinicserver.entity.TbAnnotationEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>病案附件批注信息表 前端控制器</p>
 * @datetime 2024-9-21 9:1
 */
@RestController
@RequestMapping("/ams/api/v1/annotation")
public class TbAnnotationController {
    private final static String MODULE_NAME = "病案附件批注信息表";
    private final static String TAG_NAME = "annotation";
    private final ITbAnnotationService annotationService;

    public TbAnnotationController(ITbAnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbAnnotationEntity get(@PathVariable String id) {
        TbAnnotationEntity annotation = annotationService.getById(id);
        if (annotation == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return annotation;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbAnnotationEntity save(@RequestBody TbAnnotationEntity annotation) {
        annotation.setEid(null);
        if (!annotationService.save(annotation)) {
            throw new BQApiException("新增失败");
        }
        return annotation;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbAnnotationEntity> annotations) {
        if (!annotationService.saveBatch(annotations)) {
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
    public TbAnnotationEntity update(@RequestBody TbAnnotationEntity annotation) {
        if (!annotationService.updateById(annotation)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return annotation;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbAnnotationEntity> annotations) {
        if (!annotationService.updateBatchById(annotations)) {
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
        if (!annotationService.removePhysicalById(id)) {
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
        if (!annotationService.removePhysicalBatchByIds(ids)) {
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
        if (!annotationService.removeById(id)) {
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
        if (!annotationService.removeBatchByIds(ids)) {
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
    public Iterable<TbAnnotationEntity> list(BQSearchParamsGet<TbAnnotationEntity> paramsGet) {
        BQSearchParams<TbAnnotationEntity> params = paramsGet.toSearchParams();
        return annotationService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbAnnotationEntity> page(BQSearchParamsGet<TbAnnotationEntity> paramsGet) {
        BQSearchParams<TbAnnotationEntity> params = paramsGet.toSearchParams();
        return annotationService.page(params.toPage(), params.toWrapper());
    }

    /**
     * 批注修复
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批注修复")
    @RequestMapping(value = "/fix/{annotationId}", method = RequestMethod.POST)
    public Object fix(@PathVariable String annotationId) {
        return annotationService.fix(annotationId);
    }

    /**
     * 获取扣分总数
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "获取总扣分")
    @RequestMapping(value = "/getTotalPoints/{recordId}", method = RequestMethod.GET)
    public Object getTotalPoints(@PathVariable String recordId) {
        return annotationService.getTotalPoints(recordId);
    }

    /**
     * 每个附件的批注数量
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "每个附件的批注数量")
    @RequestMapping(value = "/getCountByRecordId/{recordId}", method = RequestMethod.GET)
    public Object getCountByRecordId(@PathVariable String recordId) {
        return annotationService.getCountByRecordId(recordId);
    }
}
