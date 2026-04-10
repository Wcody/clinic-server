/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.constant.buttons.BQMenuButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQRoleButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQTenantButtons;
import com.qkplm.clinic.clinicserver.mapper.TbMenuMapper;
import com.qkplm.clinic.clinicserver.service.ITbMenuService;
import com.qkplm.clinic.clinicserver.entity.TbMenuEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>系统菜单 前端控制器</p>
 * @datetime 2024-6-25 6:53
 */
@RestController
@RequestMapping("/ams/api/v1/menu")
public class TbMenuController {
    private final static String MODULE_NAME = "菜单管理";
    private final static String TAG_NAME = "menu";
    private final ITbMenuService menuService;

    public TbMenuController(ITbMenuService menuService) {
        this.menuService = menuService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbMenuEntity get(@PathVariable String id) {
        TbMenuEntity menu = menuService.getById(id);
        if (menu == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return menu;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbMenuEntity save(@RequestBody TbMenuEntity menu) {
        menu.setEid(null);
        if (!menuService.save(menu)) {
            throw new BQApiException("新增失败");
        }
        return menu;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbMenuEntity> menus) {
        if (!menuService.saveBatch(menus)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
    * 根据ID更新单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public TbMenuEntity update(@RequestBody TbMenuEntity menu) {
        if (!menuService.updateById(menu)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return menu;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbMenuEntity> menus) {
        if (!menuService.updateBatchById(menus)) {
            throw new BQApiException("批量更新失败");
        }
        return true;
    }


    /**
    * 根据ID删除单条记录，物理删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Boolean delete(@PathVariable String id) {
        if (!menuService.removePhysicalById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录，物理删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public Boolean deleteBatch(@RequestBody Collection<Serializable> ids) {
        if (!menuService.removePhysicalBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
     * 根据ID删除单条记录，逻辑删除
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (!menuService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
     * 批量删除记录，逻辑删除，默认单次执行1000条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteLogicBatch", method = RequestMethod.POST)
    public Boolean deleteLogicBatch(@RequestBody Collection<Serializable> ids) {
        if (!menuService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<TbMenuEntity> list(BQSearchParamsGet<TbMenuEntity> paramsGet) {
        BQSearchParams<TbMenuEntity> params = paramsGet.toSearchParams();
        return menuService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbMenuEntity> page(BQSearchParamsGet<TbMenuEntity> paramsGet) {
        BQSearchParams<TbMenuEntity> params = paramsGet.toSearchParams();
        return menuService.page(params.toPage(), params.toWrapper());
    }

    /**
     * 根据用户权限获取路由树
     */
    @BQAuthMark(tag = TAG_NAME, needGrant = false)
    @BQLogMark(module = MODULE_NAME, operation = "获取路由树")
    @RequestMapping("/getRoutes")
    public ArrayNode getRoutes(HttpServletRequest request) {
        return menuService.getRoutes("0", BQJacksonUtils.newArrayNode());
    }

    /**
     * 获取角色中设置权限时用的菜单列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.SET_AUTH, BQTenantButtons.SET_MENU})
    @BQLogMark(module = MODULE_NAME, operation = "获取菜单树")
    @RequestMapping("/getRoleMenus")
    public Iterable<TbMenuEntity> getRoleMenus(HttpServletRequest request) {
        return menuService.getRoleMenus();
    }

    /**
     * 根据角色ID获取角色中设置权限时用的菜单ID列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.SET_AUTH})
    @BQLogMark(module = MODULE_NAME, operation = "获取菜单ID列表")
    @RequestMapping("/getMenuIdsBy/{roleId}")
    public Iterable<String> getMenuIdsBy(@PathVariable String roleId) {
        return menuService.getMenuIdsBy(roleId);
    }

    /**
     * 根据角色ID保存角色中设置权限时用的菜单ID列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQRoleButtons.SET_AUTH})
    @BQLogMark(module = MODULE_NAME, operation = "设置菜单ID列表")
    @RequestMapping("/saveMenuIdsBy/{roleId}")
    public int saveMenuIdsBy(@PathVariable String roleId, @RequestBody Iterable<String> menuIds) {
        TbMenuMapper mapper = (TbMenuMapper) menuService.getBaseMapper();
        mapper.deleteMenuIdsBy(roleId);
        return mapper.saveMenuIdsBy(roleId, menuIds);
    }

    /**
     * 设置状态
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.SET_STATUS})
    @BQLogMark(module = "租组管理", operation = "启用禁用")
    @RequestMapping(value = "/setStatus/{eid}/{status}", method = RequestMethod.POST)
    public Object setStatus(@PathVariable String eid, @PathVariable Boolean status) {
        return menuService.setStatus(eid, status);
    }

    /**
     * 重新加载资源
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQMenuButtons.RELOAD_RESOURCE})
    @BQLogMark(module = MODULE_NAME, operation = "重载资源")
    @RequestMapping(value = "/reloadResource", method = RequestMethod.POST)
    public Object reloadResource() throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return menuService.reloadResource();
    }
}
