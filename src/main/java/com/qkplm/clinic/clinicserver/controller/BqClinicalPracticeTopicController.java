/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.service.IBqClinicalPracticeTopicService;
import com.qkplm.clinic.clinicserver.entity.BqClinicalPracticeTopicEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>临床实践指导主题表 前端控制器</p>
 * @datetime 2026-4-14 12:56
 */
@RestController
@RequestMapping("/ams/api/v1/clinical/practice/topic")
public class BqClinicalPracticeTopicController {
    private final static String MODULE_NAME = "临床实践指导主题表";
    private final static String TAG_NAME = "clinicalPracticeTopic";
    private final IBqClinicalPracticeTopicService clinicalPracticeTopicService;

    public BqClinicalPracticeTopicController(IBqClinicalPracticeTopicService clinicalPracticeTopicService) {
        this.clinicalPracticeTopicService = clinicalPracticeTopicService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BqClinicalPracticeTopicEntity get(@PathVariable String id) {
        BqClinicalPracticeTopicEntity clinicalPracticeTopic = clinicalPracticeTopicService.getById(id);
        if (clinicalPracticeTopic == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return clinicalPracticeTopic;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BqClinicalPracticeTopicEntity save(@RequestBody BqClinicalPracticeTopicEntity clinicalPracticeTopic) {
        clinicalPracticeTopic.setId(null);
        if (!clinicalPracticeTopicService.save(clinicalPracticeTopic)) {
            throw new BQApiException("新增失败");
        }
        return clinicalPracticeTopic;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<BqClinicalPracticeTopicEntity> clinicalPracticeTopics) {
        if (!clinicalPracticeTopicService.saveBatch(clinicalPracticeTopics)) {
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
    public BqClinicalPracticeTopicEntity update(@RequestBody BqClinicalPracticeTopicEntity clinicalPracticeTopic) {
        if (!clinicalPracticeTopicService.updateById(clinicalPracticeTopic)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return clinicalPracticeTopic;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<BqClinicalPracticeTopicEntity> clinicalPracticeTopics) {
        if (!clinicalPracticeTopicService.updateBatchById(clinicalPracticeTopics)) {
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
        if (!clinicalPracticeTopicService.removePhysicalById(id)) {
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
        if (!clinicalPracticeTopicService.removePhysicalBatchByIds(ids)) {
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
        if (!clinicalPracticeTopicService.removeById(id)) {
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
        if (!clinicalPracticeTopicService.removeBatchByIds(ids)) {
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
    public Iterable<BqClinicalPracticeTopicEntity> list(BQSearchParamsGet<BqClinicalPracticeTopicEntity> paramsGet) {
        BQSearchParams<BqClinicalPracticeTopicEntity> params = paramsGet.toSearchParams();
        return clinicalPracticeTopicService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BqClinicalPracticeTopicEntity> page(BQSearchParamsGet<BqClinicalPracticeTopicEntity> paramsGet) {
        BQSearchParams<BqClinicalPracticeTopicEntity> params = paramsGet.toSearchParams();
        return clinicalPracticeTopicService.page(params.toPage(), params.toWrapper());
    }
}
