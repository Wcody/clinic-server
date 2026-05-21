/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.qkplm.clinic.clinicserver.entity.TbMenuEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
* @author Wcke
* @description <p>系统菜单 服务类</p>
* @datetime 2024-6-19 20:27
*/
public interface ITbMenuService extends IBaqiService<TbMenuEntity> {
    /**
     * 根据父级id查询按钮的权限
     */
    List<String> getButtonAuthsBy(String parentId, List<String> roleIds);

    /**
     * 根据父级id查询按钮的权限
     */
    List<String> getAllButtonAuths(List<String> roleIds);

    /**
     * 根据菜单ID列表查询指定父级下按钮权限
     */
    List<String> getButtonAuthsByMenuIds(String parentId, List<String> menuIds);

    /**
     * 根据菜单ID列表查询所有按钮权限
     */
    List<String> getAllButtonAuthsByMenuIds(List<String> menuIds);
    /**
     * 根据父级id查询菜单
     */
    Iterable<TbMenuEntity> listByParentId(String parentId);

    /**
     * 根据父级id查询菜单
     */
    Iterable<TbMenuEntity> listByParentId(String parentId, List<SFunction<TbMenuEntity, ?>> columns);

    /**
     * 根据用户权限获取路由菜单
     */
    ArrayNode getRoutes(String parentId, ArrayNode menus);

    /**
     * 获取角色中设置权限时用的菜单列表
     */
    Iterable<TbMenuEntity> getRoleMenus();

    /**
     * 根据角色ID获取菜单ID列表
     */
    List<String> getMenuIdsBy(String roleId);

    /**
     * 设置菜单禁用启用状态
     */
    Object setStatus(String eid, Boolean status);

    /**
     * 根据角色ID获取菜单ID列表
     */
    List<String> getMenuIdsBy(List<String> roleIds);

    /**
     * 获取auths和菜单Eid的映射关系
     */
    Map<String, String> getNameAndEidMap();

    /**
     * 重载资源
     */
    Boolean reloadResource() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException;
}
