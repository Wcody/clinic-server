/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.constant.buttons.BQFKindButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQMKindButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQParamItemButtons;
import com.qkplm.clinic.clinicserver.service.ITbKindService;
import com.qkplm.clinic.clinicserver.entity.TbKindEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Wcke
 * @description <p>档案分类 前端控制器</p>
 * @datetime 2024-7-12 9:43
 */
@RestController
@RequestMapping("/ams/api/v1/kind")
public class TbKindController {
    private final static String MODULE_NAME = "档案分类";
    private final static String TAG_NAME = "kind";
    private final ITbKindService kindService;

    public TbKindController(ITbKindService kindService) {
        this.kindService = kindService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFKindButtons.SEARCH, BQMKindButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbKindEntity get(@PathVariable String id) {
        TbKindEntity kind = kindService.getById(id);
        if (kind == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return kind;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFKindButtons.ADD, BQMKindButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbKindEntity save(@RequestBody TbKindEntity kind) {
        kind.setEid(null);
        if (!kindService.save(kind)) {
            throw new BQApiException("新增失败");
        }
        return kind;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFKindButtons.ADD, BQMKindButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbKindEntity> kinds) {
        if (!kindService.saveBatch(kinds)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
    * 根据ID更新单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFKindButtons.EDIT, BQMKindButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public TbKindEntity update(@RequestBody TbKindEntity kind) {
        if (!kindService.updateById(kind)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return kind;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFKindButtons.EDIT, BQMKindButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbKindEntity> kinds) {
        if (!kindService.updateBatchById(kinds)) {
            throw new BQApiException("批量更新失败");
        }
        return true;
    }


    /**
    * 根据ID删除单条记录，物理删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFKindButtons.DELETE, BQMKindButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Boolean delete(@PathVariable String id) {
        if (!kindService.removePhysicalById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录,物理删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFKindButtons.DELETE, BQMKindButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public Boolean deleteBatch(@RequestBody Collection<Serializable> ids) {
        if (!kindService.removePhysicalBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 根据ID删除单条记录，逻辑删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFKindButtons.DELETE, BQMKindButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (!kindService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录，逻辑删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFKindButtons.DELETE, BQMKindButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteLogicBatch", method = RequestMethod.POST)
    public Boolean deleteLogicBatch(@RequestBody Collection<Serializable> ids) {
        if (!kindService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFKindButtons.SEARCH, BQMKindButtons.SEARCH, BQParamItemButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<TbKindEntity> list(BQSearchParamsGet<TbKindEntity> paramsGet) {
        BQSearchParams<TbKindEntity> params = paramsGet.toSearchParams();
        return kindService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFKindButtons.SEARCH, BQMKindButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbKindEntity> page(BQSearchParamsGet<TbKindEntity> paramsGet) {
        BQSearchParams<TbKindEntity> params = paramsGet.toSearchParams();
        return kindService.page(params.toPage(), params.toWrapper());
    }

    /**
     * 设置状态
     */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQFKindButtons.SET_STATUS, BQMKindButtons.SET_STATUS })
    @BQLogMark(module = MODULE_NAME, operation = "启用禁用")
    @RequestMapping(value = "/setStatus/{eid}/{status}", method = RequestMethod.POST)
    public Object setStatus(@PathVariable String eid, @PathVariable Boolean status) {
        return kindService.setStatus(eid, status);
    }

    /**
     * 获取缺失的分类列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "获取缺失的分类列表")
    @RequestMapping(value = "/listQt", method = RequestMethod.POST)
    public Object listQt(@RequestBody  HashMap<String, Object> params) {
        return kindService.listQt(params);
    }
}
