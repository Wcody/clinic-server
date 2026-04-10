/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.constant.buttons.BQDeptButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQUserButtons;
import com.qkplm.clinic.clinicserver.service.ITbDeptService;
import com.qkplm.clinic.clinicserver.entity.TbDeptEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>部门 前端控制器</p>
 * @datetime 2024-6-25 6:53
 */
@RestController
@RequestMapping("/ams/api/v1/dept")
public class TbDeptController {
    private final static String MODULE_NAME = "部门管理";
    private final static String TAG_NAME = "dept";
    private final ITbDeptService deptService;

    public TbDeptController(ITbDeptService deptService) {
        this.deptService = deptService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQDeptButtons.SEARCH })
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbDeptEntity get(@PathVariable String id) {
        TbDeptEntity dept = deptService.getById(id);
        if (dept == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return dept;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQDeptButtons.ADD })
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbDeptEntity save(@RequestBody TbDeptEntity dept) {
        dept.setEid(null);
        if (!deptService.save(dept)) {
            throw new BQApiException("新增失败");
        }
        return dept;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQDeptButtons.ADD })
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbDeptEntity> depts) {
        if (!deptService.saveBatch(depts)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
    * 根据ID更新单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQDeptButtons.EDIT })
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public TbDeptEntity update(@RequestBody TbDeptEntity dept) {
        if (!deptService.updateById(dept)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return dept;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQDeptButtons.EDIT })
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbDeptEntity> depts) {
        if (!deptService.updateBatchById(depts)) {
            throw new BQApiException("批量更新失败");
        }
        return true;
    }


    /**
    * 根据ID删除单条记录,物理删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQDeptButtons.DELETE })
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Boolean delete(@PathVariable String id) {
        if (!deptService.removePhysicalById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录，物理删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQDeptButtons.DELETE })
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public Boolean deleteBatch(@RequestBody Collection<Serializable> ids) {
        if (!deptService.removePhysicalBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
     * 根据ID删除单条记录,逻辑删除
     */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQDeptButtons.DELETE })
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (!deptService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
     * 批量删除记录，逻辑删除，默认单次执行1000条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQDeptButtons.DELETE })
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteLogicBatch", method = RequestMethod.POST)
    public Boolean deleteLogicBatch(@RequestBody Collection<Serializable> ids) {
        if (!deptService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQDeptButtons.SEARCH, BQUserButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<TbDeptEntity> list(BQSearchParamsGet<TbDeptEntity> paramsGet) {
        BQSearchParams<TbDeptEntity> params = paramsGet.toSearchParams();
        return deptService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQDeptButtons.SEARCH })
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbDeptEntity> page(BQSearchParamsGet<TbDeptEntity> paramsGet) {
        BQSearchParams<TbDeptEntity> params = paramsGet.toSearchParams();
        return deptService.page(params.toPage(), params.toWrapper());
    }

    /**
     * 设置状态
     */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQDeptButtons.SET_STATUS })
    @BQLogMark(module = MODULE_NAME, operation = "启用禁用")
    @RequestMapping(value = "/setStatus/{eid}/{status}", method = RequestMethod.POST)
    public Object setStatus(@PathVariable String eid, @PathVariable Boolean status) {
        return deptService.setStatus(eid, status);
    }
}
