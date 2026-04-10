/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.constant.buttons.BQTenantButtons;
import com.qkplm.clinic.clinicserver.service.IBqParamItemService;
import com.qkplm.clinic.clinicserver.service.ITbTenantService;
import com.qkplm.clinic.clinicserver.entity.TbTenantEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Wcke
 * @description <p>系统租户 前端控制器</p>
 * @datetime 2024-6-25 6:53
 */
@RestController
@RequestMapping("/ams/api/v1/tenant")
public class TbTenantController {
    private final static String MODULE_NAME = "租户管理";
    private final static String TAG_NAME = "tenant";
    private final ITbTenantService tenantService;
    private final IBqParamItemService paramItemService;

    public TbTenantController(ITbTenantService tenantService, IBqParamItemService paramItemService) {
        this.tenantService = tenantService;
        this.paramItemService = paramItemService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbTenantEntity get(@PathVariable String id) {
        TbTenantEntity tenant = tenantService.getById(id);
        if (tenant == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return tenant;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbTenantEntity save(@RequestBody TbTenantEntity tenant) {
        tenant.setEid(null);
        if (!tenantService.save(tenant)) {
            throw new BQApiException("新增失败");
        }
        return tenant;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbTenantEntity> tenants) {
        if (!tenantService.saveBatch(tenants)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
    * 根据ID更新单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public TbTenantEntity update(@RequestBody TbTenantEntity tenant) {
        if (!tenantService.updateById(tenant)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return tenant;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbTenantEntity> tenants) {
        if (!tenantService.updateBatchById(tenants)) {
            throw new BQApiException("批量更新失败");
        }
        return true;
    }


    /**
    * 根据ID删除单条记录，物理删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Boolean delete(@PathVariable String id) {
        if (tenantService.removePhysicalById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录，物理删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public Boolean deleteBatch(@RequestBody Collection<Serializable> ids) {
        if (!tenantService.removePhysicalBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
     * 根据ID删除单条记录，逻辑删除
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (tenantService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
     * 批量删除记录，逻辑删除，默认单次执行1000条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteLogicBatch", method = RequestMethod.POST)
    public Boolean deleteLogicBatch(@RequestBody Collection<Serializable> ids) {
        if (!tenantService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<TbTenantEntity> list(BQSearchParamsGet<TbTenantEntity> paramsGet) {
        BQSearchParams<TbTenantEntity> params = paramsGet.toSearchParams();
        return tenantService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbTenantEntity> page(BQSearchParamsGet<TbTenantEntity> paramsGet) {
        BQSearchParams<TbTenantEntity> params = paramsGet.toSearchParams();
        return tenantService.page(params.toPage(), params.toWrapper());
    }

    /**
     * 设置租户管理员
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.SET_ADMIN})
    @BQLogMark(module = MODULE_NAME, operation = "设置租户管理员")
    @RequestMapping(value = "/saveUserIds/{tenantId}", method = RequestMethod.POST)
    public Object saveUserIds(@PathVariable String tenantId, @RequestBody List<String> userIds) {
        return tenantService.saveUserIds(tenantId, userIds);
    }

    /**
     * 获取租户管理员
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.SET_ADMIN})
    @BQLogMark(module = MODULE_NAME, operation = "获取租户管理员")
    @RequestMapping(value = "/getUserIdsBy/{tenantId}", method = RequestMethod.GET)
    public Object getUserIdsBy(@PathVariable String tenantId) {
        return tenantService.getUserIdsBy(tenantId);
    }

    /**
     * 设置状态
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.SET_STATUS})
    @BQLogMark(module = MODULE_NAME, operation = "启用禁用")
    @RequestMapping(value = "/setStatus/{eid}/{status}", method = RequestMethod.POST)
    public Object setStatus(@PathVariable String eid, @PathVariable Boolean status) {
        return tenantService.setStatus(eid, status);
    }

    /**
     * 设置租户可用菜单
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.SET_MENU})
    @BQLogMark(module = MODULE_NAME, operation = "设置可用菜单")
    @RequestMapping(value = "/saveMenuIds/{tenantId}", method = RequestMethod.POST)
    public Object saveMenuIds(@PathVariable String tenantId, @RequestBody List<String> menuIds) {
        return tenantService.saveMenuIds(tenantId, menuIds);
    }

    /**
     * 获取租户可用菜单
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQTenantButtons.SET_MENU})
    @BQLogMark(module = MODULE_NAME, operation = "获取可用菜单")
    @RequestMapping(value = "/getMenuIdsBy/{tenantId}", method = RequestMethod.GET)
    public Object getMenuIdsBy(@PathVariable String tenantId) {
        return tenantService.getMenuIdsBy(tenantId);
    }

    /**
     * 获取租户的名称和Logo
     */
    @BQAuthMark(tag = TAG_NAME, needGrant = false)
    @BQLogMark(module = MODULE_NAME, operation = "获取租户的名称和Logo")
    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    public Object getInfo() {
        return paramItemService.getTenantInfo();
    }
}
