/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import com.qkplm.clinic.clinicserver.entity.TbMenuEntity;
import com.qkplm.clinic.clinicserver.enums.BQMenuTypeEnum;
import com.qkplm.clinic.clinicserver.mapper.TbMenuMapper;
import com.qkplm.clinic.clinicserver.service.ITbMenuService;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.base.BQButton;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import com.qkplm.clinic.libcommon.security.BQSecurityPermissionUtils;
import com.qkplm.clinic.libcommon.security.BQSecurityUserDetails;
import com.qkplm.clinic.libcommon.utils.BQAuthUtils;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;
import com.qkplm.clinic.libcommon.utils.BQReflectionUtils;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Wcke
 * @description
 *              <p>
 *              系统菜单 服务接口类
 *              </p>
 * @datetime 2024-6-19 20:27
 */
@Service
public class TbMenuServiceImpl extends BaqiServiceImpl<TbMenuMapper, TbMenuEntity> implements ITbMenuService {

    @Override
    public List<String> getButtonAuthsBy(String parentId, List<String> roleIds) {
        return getBaseMapper().getButtonAuthsBy(parentId, roleIds);
    }

    @Override
    public List<String> getAllButtonAuths(List<String> roleIds) {
        return getBaseMapper().getAllButtonAuths(roleIds);
    }

    @Override
    public Iterable<TbMenuEntity> listByParentId(String parentId) {
        return listByParentId(parentId, null);
    }

    @Override
    public final Iterable<TbMenuEntity> listByParentId(String parentId, List<SFunction<TbMenuEntity, ?>> columns) {
        LambdaQueryWrapper<TbMenuEntity> wrapper = new LambdaQueryWrapper<>();
        if (!Objects.isNull(columns)) {
            wrapper.select(columns);
        }
        wrapper.eq(TbMenuEntity::getParentId, parentId);
        wrapper.orderByAsc(TbMenuEntity::getOrderValue);
        return this.list(wrapper);
    }

    @Override
    public ArrayNode getRoutes(String parentId, ArrayNode menus) {
        BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
        Iterable<TbMenuEntity> list = this.listByParentId(parentId);
        for (TbMenuEntity menu : list) {
            boolean isMenu = !Objects.equals(menu.getMenuType(), BQMenuTypeEnum.BUTTON.getCode());
            boolean hasPermission = userDetails.isSuperAccount() || userDetails.isAdminAccount()
                    || userDetails.getMenus().contains(menu.getEid());
            if (isMenu && hasPermission && menu.getStatus()) {
                if (userDetails.isAdminAccount() && !BQAuthUtils.isAuthMenu(menu.getEid())) {
                    // 对admin放行授权的菜单
                    continue;
                }
                ObjectNode node = BQJacksonUtils.newObjectNode();
                node.put("path", menu.getPath());
                // 顶层不能返回名称
                if (!Objects.equals(parentId, "0")) {
                    node.put("name", menu.getName());
                }
                if (!StringUtils.isBlank(menu.getComponent())) {
                    node.put("component", menu.getComponent());
                }
                List<String> auths = getButtonAuthsBy(menu.getEid(), userDetails.getRoles());
                if (userDetails.isSuperAccount()) {
                    // 返回最大权限
                    auths = new ArrayList<>(auths);
                    auths.add("super");
                } else if (userDetails.isAdminAccount()) {
                    auths = new ArrayList<>(auths);
                    auths.add("admin");
                } else {
                    auths = new ArrayList<>(auths);
                    auths.add("common");
                }
                // 解开逗号
                List<String> extractAuth = new ArrayList<>();
                for (String auth : auths) {
                    if (!StringUtils.isBlank(auth)) {
                        extractAuth.addAll(List.of(auth.split(",")));
                    }
                }
                node.putPOJO("meta", BQJacksonUtils.newObjectNode()
                        .put("title", menu.getTitle())
                        .put("icon", menu.getIcon())
                        .putPOJO("auths", extractAuth)
                        .put("rank", menu.getOrderValue())
                        .put("keepAlive", menu.getKeepAlive()));

                ArrayNode children = getRoutes(menu.getEid(), BQJacksonUtils.newArrayNode());
                if (!children.isEmpty()) {
                    node.putPOJO("children", children);
                }
                menus.add(node);
            }
        }
        return menus;
    }

    @Override
    public Iterable<TbMenuEntity> getRoleMenus() {
        BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
        LambdaQueryWrapper<TbMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TbMenuEntity::getEid, TbMenuEntity::getParentId, TbMenuEntity::getTitle,
                TbMenuEntity::getRemark);
        if (!userDetails.isSuperAccount()) {
            wrapper.in(TbMenuEntity::getEid, BQAuthUtils.getMenuIds());
        }
        wrapper.orderByAsc(TbMenuEntity::getOrderValue);
        return this.list(wrapper);
    }

    @Override
    public List<String> getMenuIdsBy(String roleId) {
        return this.getBaseMapper().getMenuIdsBy(roleId);
    }

    @Override
    public Object setStatus(String eid, Boolean status) {
        TbMenuEntity menu = getById(eid);
        if (Objects.nonNull(menu)) {
            menu.setStatus(status);
            return updateById(menu);
        }
        throw new BQApiException("目标不存在");
    }

    @Override
    public List<String> getMenuIdsBy(List<String> roleIds) {
        return getBaseMapper().getMenuIdsBys(roleIds);
    }

    @Override
    public Map<String, String> getNameAndEidMap() {
        LambdaQueryWrapper<TbMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TbMenuEntity::getEid, TbMenuEntity::getName);
        wrapper.eq(TbMenuEntity::getMenuType, 0);
        List<TbMenuEntity> list = this.list(wrapper);
        return list.stream().collect(Collectors.toMap(TbMenuEntity::getName, TbMenuEntity::getEid));
    }

    @Override
    public Boolean reloadResource()
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        // 菜单的键值对
        Map<String, String> map = getNameAndEidMap();
        if (map.isEmpty()) {
            throw new BQApiException("菜单模块中的主菜单未定义");
        }
        // 获取所有菜单对应的按钮{key:[string]}
        ObjectNode buttons = BQReflectionUtils.getConstantButtons("com.qkplm.clinic.clinicserver.constant.buttons");
        if (buttons.isEmpty()) {
            throw new BQApiException("constant.buttons的菜单按钮未定义");
        }
        // 需要新增或更新的按钮列表
        List<TbMenuEntity> menus = new ArrayList<>();
        // 遍历
        buttons.fields().forEachRemaining(entry -> {
            String parentId = map.get(entry.getKey());
            if (Objects.nonNull(parentId)) {
                ArrayNode arrayNode = (ArrayNode) entry.getValue();
                final int[] order = { 0 };
                int step = 10;
                arrayNode.forEach(node -> {
                    BQButton button;
                    try {
                        button = BQJacksonUtils.getMapper().treeToValue(node, BQButton.class);
                    } catch (JsonProcessingException e) {
                        throw new BQApiException(e.getMessage());
                    }
                    order[0] += step;
                    String buttonName = button.getName();
                    LambdaQueryWrapper<TbMenuEntity> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(TbMenuEntity::getParentId, parentId);
                    wrapper.eq(TbMenuEntity::getName, buttonName);
                    wrapper.eq(TbMenuEntity::getMenuType, BQMenuTypeEnum.BUTTON.getCode());
                    TbMenuEntity menu = getOne(wrapper);
                    if (Objects.isNull(menu)) {
                        menu = new TbMenuEntity();
                    }
                    menu.setParentId(parentId);
                    menu.setOrderValue(order[0]);
                    menu.setTitle(button.getTitle());
                    menu.setRemark(button.getDescription());
                    menu.setName(buttonName);
                    menu.setMenuType(BQMenuTypeEnum.BUTTON.getCode());
                    menu.setAuths(BQSecurityPermissionUtils.getButtonAuths(buttonName));
                    menu.setPath("");
                    menu.setStatus(true);
                    menu.setComponent("");
                    menu.setRedirect("");
                    menu.setIcon("");
                    menu.setEnterTransition("");
                    menu.setLeaveTransition("");
                    menu.setActivePath("");
                    menu.setFrameSrc("");
                    menus.add(menu);
                });
            }
        });
        if (!menus.isEmpty()) {
            return saveOrUpdateBatch(menus);
        }
        return true;
    }
}
