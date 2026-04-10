/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.constant.buttons.BQCustomerButtons;
import com.qkplm.clinic.clinicserver.service.ITbCustomerService;
import com.qkplm.clinic.clinicserver.entity.TbCustomerEntity;
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
 * @description <p>使用系统的客户 前端控制器</p>
 * @datetime 2024-7-23 10:28
 */
@RestController
@RequestMapping("/ams/api/v1/customer")
public class TbCustomerController {
    private final static String MODULE_NAME = "使用系统的客户";
    private final static String TAG_NAME = "customer";
    private final ITbCustomerService customerService;

    public TbCustomerController(ITbCustomerService customerService) {
        this.customerService = customerService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbCustomerEntity get(@PathVariable String id) {
        TbCustomerEntity customer = customerService.getById(id);
        if (customer == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return customer;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbCustomerEntity save(@RequestBody TbCustomerEntity customer) {
        customer.setEid(null);
        if (!customerService.save(customer)) {
            throw new BQApiException("新增失败");
        }
        return customer;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbCustomerEntity> customers) {
        if (!customerService.saveBatch(customers)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
    * 根据ID更新单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public TbCustomerEntity update(@RequestBody TbCustomerEntity customer) {
        if (!customerService.updateById(customer)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return customer;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbCustomerEntity> customers) {
        if (!customerService.updateBatchById(customers)) {
            throw new BQApiException("批量更新失败");
        }
        return true;
    }


    /**
    * 根据ID删除单条记录，物理删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Boolean delete(@PathVariable String id) {
        if (!customerService.removePhysicalById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录,物理删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public Boolean deleteBatch(@RequestBody Collection<Serializable> ids) {
        if (!customerService.removePhysicalBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 根据ID删除单条记录，逻辑删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (!customerService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录，逻辑删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteLogicBatch", method = RequestMethod.POST)
    public Boolean deleteLogicBatch(@RequestBody Collection<Serializable> ids) {
        if (!customerService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<TbCustomerEntity> list(BQSearchParamsGet<TbCustomerEntity> paramsGet) {
        BQSearchParams<TbCustomerEntity> params = paramsGet.toSearchParams();
        return customerService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbCustomerEntity> page(BQSearchParamsGet<TbCustomerEntity> paramsGet) {
        BQSearchParams<TbCustomerEntity> params = paramsGet.toSearchParams();
        return customerService.page(params.toPage(), params.toWrapper());
    }

    /**
     * 设置租户管理员
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.SET_ADMIN})
    @BQLogMark(module = MODULE_NAME, operation = "设置租户管理员")
    @RequestMapping(value = "/saveUserIds/{customerId}", method = RequestMethod.POST)
    public Object saveUserIds(@PathVariable String customerId, @RequestBody List<String> userIds) {
        return customerService.saveUserIds(customerId, userIds);
    }

    /**
     * 获取租户管理员
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.SET_ADMIN})
    @BQLogMark(module = MODULE_NAME, operation = "获取租户管理员")
    @RequestMapping(value = "/getUserIdsBy/{customerId}", method = RequestMethod.GET)
    public Object getUserIdsBy(@PathVariable String customerId) {
        return customerService.getUserIdsBy(customerId);
    }

    /**
     * 设置状态
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.SET_STATUS})
    @BQLogMark(module = MODULE_NAME, operation = "启用禁用")
    @RequestMapping(value = "/setStatus/{eid}/{status}", method = RequestMethod.POST)
    public Object setStatus(@PathVariable String eid, @PathVariable Boolean status) {
        return customerService.setStatus(eid, status);
    }

    /**
     * 设置租户可用菜单
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.SET_MENU})
    @BQLogMark(module = MODULE_NAME, operation = "设置可用菜单")
    @RequestMapping(value = "/saveMenuIds/{customerId}", method = RequestMethod.POST)
    public Object saveMenuIds(@PathVariable String customerId, @RequestBody List<String> menuIds) {
        return customerService.saveMenuIds(customerId, menuIds);
    }

    /**
     * 获取租户可用菜单
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQCustomerButtons.SET_MENU})
    @BQLogMark(module = MODULE_NAME, operation = "获取可用菜单")
    @RequestMapping(value = "/getMenuIdsBy/{customerId}", method = RequestMethod.GET)
    public Object getMenuIdsBy(@PathVariable String customerId) {
        return customerService.getMenuIdsBy(customerId);
    }
}
