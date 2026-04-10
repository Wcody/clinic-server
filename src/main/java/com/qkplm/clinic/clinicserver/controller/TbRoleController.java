/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.constant.buttons.BQRoleButtons;
import com.qkplm.clinic.clinicserver.service.ITbRoleService;
import com.qkplm.clinic.clinicserver.entity.TbRoleEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>系统角色 前端控制器</p>
 * @datetime 2024-6-25 6:53
 */
@RestController
@RequestMapping("/ams/api/v1/role")
public class TbRoleController {
    private final static String MODULE_NAME = "角色管理";
    private final static String TAG_NAME = "role";
    private final ITbRoleService roleService;

    public TbRoleController(ITbRoleService roleService) {
        this.roleService = roleService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbRoleEntity get(@PathVariable String id) {
        TbRoleEntity role = roleService.getById(id);
        if (role == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return role;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbRoleEntity save(@RequestBody TbRoleEntity role) {
        role.setEid(null);
        if (!roleService.save(role)) {
            throw new BQApiException("新增失败");
        }
        return role;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbRoleEntity> roles) {
        if (!roleService.saveBatch(roles)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
    * 根据ID更新单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public TbRoleEntity update(@RequestBody TbRoleEntity role) {
        if (!roleService.updateById(role)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return role;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbRoleEntity> roles) {
        if (!roleService.updateBatchById(roles)) {
            throw new BQApiException("批量更新失败");
        }
        return true;
    }


    /**
    * 根据ID删除单条记录，物理删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Boolean delete(@PathVariable String id) {
        if (roleService.removePhysicalById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录，物理删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public Boolean deleteBatch(@RequestBody Collection<Serializable> ids) {
        if (!roleService.removePhysicalByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
     * 根据ID删除单条记录，逻辑删除
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (roleService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
     * 批量删除记录，逻辑删除，默认单次执行1000条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteLogicBatch", method = RequestMethod.POST)
    public Boolean deleteLogicBatch(@RequestBody Collection<Serializable> ids) {
        if (!roleService.removeByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<TbRoleEntity> list(BQSearchParamsGet<TbRoleEntity> paramsGet) {
        BQSearchParams<TbRoleEntity> params = paramsGet.toSearchParams();
        return roleService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbRoleEntity> page(BQSearchParamsGet<TbRoleEntity> paramsGet) {
        BQSearchParams<TbRoleEntity> params = paramsGet.toSearchParams();
        return roleService.page(params.toPage(), params.toWrapper());
    }



    /**
     * 设置状态
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.SET_STATUS})
    @BQLogMark(module = MODULE_NAME, operation = "启用禁用")
    @RequestMapping(value = "/setStatus/{eid}/{status}", method = RequestMethod.POST)
    public Object setStatus(@PathVariable String eid, @PathVariable Boolean status) {
        return roleService.setStatus(eid, status);
    }
}
