/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.constant.buttons.BQOnlineButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQTenantButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQUserButtons;
import com.qkplm.clinic.clinicserver.entity.TbUserEntity;
import com.qkplm.clinic.clinicserver.mapper.TbUserMapper;
import com.qkplm.clinic.clinicserver.service.ITbDeptService;
import com.qkplm.clinic.clinicserver.service.ITbUserService;
import com.qkplm.clinic.clinicserver.dtos.BQUserKickOutDto;
import com.qkplm.clinic.clinicserver.dtos.BQUserOnlineListDto;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;
import com.qkplm.clinic.libcommon.security.BQSecurityUserDetails;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Wcke
 * @description <p>系统用户 前端控制器</p>
 * @datetime 2024-6-20 22:16
 */
@RestController
@RequestMapping("/ams/api/v1/user")
public class TbUserController {
    private final static String MODULE_NAME = "用户管理";
    private final static String TAG_NAME = "user";
    private final ITbUserService userService;
    private final ITbDeptService deptService;

    public TbUserController(ITbUserService userService, ITbDeptService deptService) {
        this.userService = userService;
        this.deptService = deptService;
    }

    /**
     * 根据ID获取单条记录
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbUserEntity get(@PathVariable String id) {
        TbUserEntity user = userService.getById(id);
        if (user == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return user;
    }

    /**
     * 获取当前用户的记录
     */
    @BQAuthMark(tag = TAG_NAME, needGrant = false)
    @BQLogMark(module = MODULE_NAME, operation = "查询(自己的)")
    @RequestMapping(value = "/getMine", method = RequestMethod.GET)
    public TbUserEntity getMine() {
        BQRequestContextHolderUtils.setSourceTenantId();
        try {
            BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
            TbUserEntity user = userService.getById(userDetails.getEid());
            if (user == null) {
                throw new BQApiException("获取失败，对象可能不存在");
            }
            return user;
        } finally {
            BQRequestContextHolderUtils.removeSourceTenantId();
        }
    }

    /**
     * 新增单条记录
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbUserEntity save(@RequestBody TbUserEntity user) {
        user.setEid(null);
        if (!userService.saveWithPassword(user)) {
            throw new BQApiException("新增失败");
        }
        return user;
    }

    /**
     * 批量新增记录，默认单次执行1000条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.ADD})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbUserEntity> users) {
        if (!userService.saveBatchWithPassword(users)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
     * 根据ID更新单条记录，物理删除
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public TbUserEntity update(@RequestBody TbUserEntity user) {
        if (!userService.updateById(user)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return user;
    }

    /**
     * 根据更新当前登录用户信息
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "更新(自己)的")
    @RequestMapping(value = "/updateMine", method = RequestMethod.POST)
    public TbUserEntity updateMine(@RequestBody TbUserEntity user) {
        BQRequestContextHolderUtils.setSourceTenantId();
        try {
            BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
            TbUserEntity updateUser = new TbUserEntity();
            updateUser.setEid(userDetails.getEid());
            updateUser.setNickname(user.getNickname());
            updateUser.setAvatar(user.getAvatar());
            updateUser.setRemark(user.getRemark());
            updateUser.setPhone(user.getPhone());
            updateUser.setEmail(user.getEmail());
            updateUser.setSex(user.getSex());
            if (!userService.updateById(user)) {
                throw new BQApiException("更新失败，对象可能已失效");
            }
            return user;
        } finally {
            BQRequestContextHolderUtils.removeSourceTenantId();
        }
    }

    /**
     * 批量更新记录，物理删除，默认单次执行1000条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbUserEntity> users) {
        if (!userService.updateBatchById(users)) {
            throw new BQApiException("批量更新失败");
        }
        return true;
    }


    /**
     * 根据ID删除单条记录，逻辑删除
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (!userService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
     * 批量删除记录，逻辑删除，默认单次执行1000条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.DELETE})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteLogicBatch", method = RequestMethod.POST)
    public Boolean deleteLogicBatch(@RequestBody Collection<Serializable> ids) {
        if (!userService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
     * 列表查询，orders可以排序，filters可以过滤
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.SEARCH, BQTenantButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<TbUserEntity> list(BQSearchParamsGet<TbUserEntity> paramsGet) {
        BQSearchParams<TbUserEntity> params = paramsGet.toSearchParams();
        return deptService.getDeptInfo(userService.list(params.toPage(), params.toWrapper()));
    }

    /**
     * 分页查询，orders可以排序，filters可以过滤
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbUserEntity> page(BQSearchParamsGet<TbUserEntity> paramsGet) {
        BQSearchParams<TbUserEntity> params = paramsGet.toSearchParams();
        Page<TbUserEntity> page = userService.page(params.toPage(), params.toWrapper());
        deptService.getDeptInfo(page.getRecords());
        return page;
    }

    /**
     * 重置密码
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.RESET_PASS})
    @BQLogMark(module = MODULE_NAME, operation = "重置密码")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public Object resetPassword(@RequestBody List<String> infos) {
        return userService.resetPassword(infos);
    }

    /**
     * 修改密码
     */
    @BQAuthMark(tag = TAG_NAME, needGrant = false)
    @BQLogMark(module = MODULE_NAME, operation = "修改密码")
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public Object changePassword(@RequestBody List<String> passwords) {
        return userService.changePassword(passwords);
    }

    /**
     * 设置用户所属角色列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.SET_ROLES})
    @BQLogMark(module = MODULE_NAME, operation = "设置角色ID列表")
    @RequestMapping(value = "/saveRoleIds/{userId}", method = RequestMethod.POST)
    public Object saveRoleIds(@PathVariable String userId, @RequestBody List<String> roleIds) {
        return userService.saveRoleIds(userId, roleIds);
    }

    /**
     * 根据用户ID获取所属角色ID列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.SET_ROLES})
    @BQLogMark(module = MODULE_NAME, operation = "获取角色ID列表")
    @RequestMapping(value = "/getRoleIdsBy/{userId}", method = RequestMethod.GET)
    public Object getRoleIdsBy(@PathVariable String userId) {
        TbUserMapper mapper = (TbUserMapper) userService.getBaseMapper();
        return mapper.getRoleIdsBy(userId);
    }

    /**
     * 禁用、启用用户
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.SET_STATUS})
    @BQLogMark(module = MODULE_NAME, operation = "启用禁用")
    @RequestMapping(value = "/setStatus/{eid}/{status}", method = RequestMethod.POST)
    public Object setStatus(@PathVariable String eid, @PathVariable Boolean status) {
        return userService.setStatus(eid, status);
    }

    /**
     * 查看在线用户
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQOnlineButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "查看在线用户")
    @RequestMapping(value = "/onlineList", method = RequestMethod.GET)
    public Object onlineList(BQUserOnlineListDto params) {
        return userService.onlineList(params);
    }

    /**
     * 强退在线用户
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQOnlineButtons.KICK_OUT})
    @BQLogMark(module = MODULE_NAME, operation = "强退在线用户")
    @RequestMapping(value = "/kickOut", method = RequestMethod.GET)
    public Object kickOut(BQUserKickOutDto params) {
        return userService.kickOut(params);
    }
}
