/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.qkplm.clinic.clinicserver.constant.buttons.BQLogLoginButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQLogLogoffButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQLogOperationButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQLogSystemButtons;
import com.qkplm.clinic.clinicserver.service.IBqLogService;
import com.qkplm.clinic.clinicserver.entity.BqLogEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;
import com.qkplm.clinic.libcommon.enums.BQLogTypeEnum;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Wcke
 * @description <p>系统日志 前端控制器</p>
 * @datetime 2024-6-26 17:19
 */
@RestController
@RequestMapping("/ams/api/v1/log")
public class BqLogController {
    private final static String MODULE_NAME = "系统日志";
    private final static String TAG_NAME = "log";
    private final IBqLogService logService;

    public BqLogController(IBqLogService logService) {
        this.logService = logService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {
            BQLogLoginButtons.SEARCH,
            BQLogLogoffButtons.SEARCH,
            BQLogOperationButtons.SEARCH,
            BQLogSystemButtons.SEARCH,
    })
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BqLogEntity get(@PathVariable String id) {
        BqLogEntity log = logService.getById(id);
        if (log == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return log;
    }

    /**
    * 根据ID删除单条记录，物理删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {
            BQLogLoginButtons.DELETE,
            BQLogLogoffButtons.DELETE,
            BQLogOperationButtons.DELETE,
            BQLogSystemButtons.DELETE,
    })
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Boolean delete(@PathVariable String id) {
        if (!logService.removePhysicalById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录，物理删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {
            BQLogLoginButtons.DELETE,
            BQLogLogoffButtons.DELETE,
            BQLogOperationButtons.DELETE,
            BQLogSystemButtons.DELETE,
    })
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public Boolean deleteBatch(@RequestBody Collection<Serializable> ids) {
        if (!logService.removePhysicalBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
     * 根据ID删除单条记录，逻辑删除
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {
            BQLogLoginButtons.DELETE,
            BQLogLogoffButtons.DELETE,
            BQLogOperationButtons.DELETE,
            BQLogSystemButtons.DELETE,
    })
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (!logService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
     * 批量删除记录，逻辑删除，默认单次执行1000条
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {
            BQLogLoginButtons.DELETE,
            BQLogLogoffButtons.DELETE,
            BQLogOperationButtons.DELETE,
            BQLogSystemButtons.DELETE,
    })
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteLogicBatch", method = RequestMethod.POST)
    public Boolean deleteLogicBatch(@RequestBody Collection<Serializable> ids) {
        if (!logService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {
            BQLogLoginButtons.SEARCH,
            BQLogLogoffButtons.SEARCH,
            BQLogOperationButtons.SEARCH,
            BQLogSystemButtons.SEARCH,
    })
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<BqLogEntity> list(BQSearchParamsGet<BqLogEntity> paramsGet) {
        BQSearchParams<BqLogEntity> params = paramsGet.toSearchParams();
        return logService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {
            BQLogLoginButtons.SEARCH,
            BQLogLogoffButtons.SEARCH,
            BQLogOperationButtons.SEARCH,
            BQLogSystemButtons.SEARCH,
    })
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BqLogEntity> page(BQSearchParamsGet<BqLogEntity> paramsGet) {
        BQSearchParams<BqLogEntity> params = paramsGet.toSearchParams();
        return logService.page(params.toPage(), params.toWrapper());
    }

    /**
     * 查询登录日志，orders可以排序，filters可以过滤
     */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQLogLoginButtons.SEARCH })
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/pageLogin", method = RequestMethod.GET)
    public Page<BqLogEntity> pageLogin(BQSearchParamsGet<BqLogEntity> paramsGet) {
        BQSearchParams<BqLogEntity> params = paramsGet.toSearchParams();
        QueryWrapper<BqLogEntity> wrapper = params.toWrapper();
        wrapper.select("id", "createdTime", "logType", "status",
                "costTime", "requestIp", "exceptionInfo", "account",
                "os", "platform", "browser");
        wrapper.eq("logType", BQLogTypeEnum.LOGIN.getCode());
        return logService.page(params.toPage(), wrapper);
    }

    /**
     * 查询操作日志，orders可以排序，filters可以过滤
     */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQLogOperationButtons.SEARCH })
    @BQLogMark(module = MODULE_NAME, operation = "查询操作日志")
    @RequestMapping(value = "/pageOperation", method = RequestMethod.GET)
    public Page<BqLogEntity> pageOperation(BQSearchParamsGet<BqLogEntity> paramsGet) {
        BQSearchParams<BqLogEntity> params = paramsGet.toSearchParams();
        QueryWrapper<BqLogEntity> wrapper = params.toWrapper();
        wrapper.select("id", "createdTime", "status", "module", "operation",
                "costTime", "requestIp", "exceptionInfo", "account",
                "os", "platform", "browser", "requestAddress");
        wrapper.eq("logType", BQLogTypeEnum.OPERATION.getCode());
        return logService.page(params.toPage(), wrapper);
    }

    /**
     * 查询系统日志，orders可以排序，filters可以过滤
     */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQLogSystemButtons.SEARCH })
    @BQLogMark(module = MODULE_NAME, operation = "查询系统日志")
    @RequestMapping(value = "/pageSystem", method = RequestMethod.GET)
    public Page<BqLogEntity> pageSystem(BQSearchParamsGet<BqLogEntity> paramsGet) {
        BQSearchParams<BqLogEntity> params = paramsGet.toSearchParams();
        QueryWrapper<BqLogEntity> wrapper = params.toWrapper();
        wrapper.select("id", "createdTime", "status", "module", "operation",
                "costTime", "requestIp", "exceptionInfo", "account", "requestUri",
                "requestAddress", "os", "platform", "browser", "method");
        wrapper.eq("logType", BQLogTypeEnum.SYSTEM.getCode());
        return logService.page(params.toPage(), wrapper);
    }

    /**
     * 查询注销日志
     */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQLogLogoffButtons.SEARCH })
    @BQLogMark(module = MODULE_NAME, operation = "查询注销日志")
    @RequestMapping(value = "/pageLogoff", method = RequestMethod.GET)
    public Page<BqLogEntity> pageLogoff(BQSearchParamsGet<BqLogEntity> paramsGet) {
        BQSearchParams<BqLogEntity> params = paramsGet.toSearchParams();
        QueryWrapper<BqLogEntity> wrapper = params.toWrapper();
        wrapper.select("id", "createdTime", "logType", "status",
                "costTime", "requestIp", "exceptionInfo", "account",
                "os", "platform", "browser");
        wrapper.eq("logType", BQLogTypeEnum.LOGOFF.getCode());
        return logService.page(params.toPage(), wrapper);
    }

    /**
     * 清空登录日志
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {
            BQLogLoginButtons.CLEAR
    })
    @BQLogMark(module = MODULE_NAME, operation = "清空登录日志")
    @RequestMapping(value = "/clearLoginLog", method = RequestMethod.POST)
    public Object clearLoginLog() {
        QueryWrapper<BqLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("logType", BQLogTypeEnum.LOGIN.getCode());
        return logService.remove(wrapper);
    }

    /**
     * 清空操作日志
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {
            BQLogOperationButtons.CLEAR
    })
    @BQLogMark(module = MODULE_NAME, operation = "清空操作日志")
    @RequestMapping(value = "/clearOperationLog", method = RequestMethod.POST)
    public Object clearOperationLog() {
        QueryWrapper<BqLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("logType", BQLogTypeEnum.OPERATION.getCode());
        return logService.remove(wrapper);
    }

    /**
     * 清空系统日志
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {
            BQLogSystemButtons.CLEAR
    })
    @BQLogMark(module = MODULE_NAME, operation = "清空系统日志")
    @RequestMapping(value = "/clearSystemLog", method = RequestMethod.POST)
    public Object clearSystemLog() {
        QueryWrapper<BqLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("logType", BQLogTypeEnum.SYSTEM.getCode());
        return logService.remove(wrapper);
    }

    /**
     * 清空注销日志
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {
            BQLogLogoffButtons.CLEAR,
    })
    @BQLogMark(module = MODULE_NAME, operation = "清空注销日志")
    @RequestMapping(value = "/clearLogoffLog", method = RequestMethod.POST)
    public Object clearLogoffLog() {
        QueryWrapper<BqLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("logType", BQLogTypeEnum.LOGOFF.getCode());
        return logService.remove(wrapper);
    }
}
