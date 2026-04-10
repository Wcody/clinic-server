/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.qkplm.clinic.clinicserver.constant.buttons.BQParamItemButtons;
import com.qkplm.clinic.clinicserver.service.IBqParamItemService;
import com.qkplm.clinic.clinicserver.entity.BqParamItemEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;
import com.qkplm.clinic.libcommon.utils.BQIDUtils;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;
import com.qkplm.clinic.libcommon.utils.BQUploadHandlerUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Wcke
 * @description <p>系统参数项表 前端控制器</p>
 * @datetime 2024-9-21 9:1
 */
@RestController
@RequestMapping("/ams/api/v1/param/item")
public class BqParamItemController {
    private final static String MODULE_NAME = "系统参数项表";
    private final static String TAG_NAME = "paramItem";
    private final IBqParamItemService paramItemService;

    public BqParamItemController(IBqParamItemService paramItemService) {
        this.paramItemService = paramItemService;
    }

    private ArrayList<String> getImagePathsByFiles(MultipartFile[] files) {
        try {
            ArrayList<String> paths = new ArrayList<>();
            for (MultipartFile file : files) {
                String filename = file.getOriginalFilename();
                String suffix = Objects.requireNonNull(filename).substring(filename.lastIndexOf("."));
                filename = BQIDUtils.uuid() + suffix;
                filename = BQUploadHandlerUtils.getSubPath(filename);
                File subFile = BQUploadHandlerUtils.getUploadImageBySubPath(filename);
                file.transferTo(subFile);
                paths.add(filename);
            }
            return paths;
        } catch (Exception e) {
            throw new BQApiException("图片上传失败：" + e.getMessage());
        }
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQParamItemButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BqParamItemEntity get(@PathVariable String id) {
        BqParamItemEntity paramItem = paramItemService.getById(id);
        if (paramItem == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return paramItem;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BqParamItemEntity save(@RequestBody BqParamItemEntity paramItem) {
        if (!paramItemService.save(paramItem)) {
            throw new BQApiException("新增失败");
        }
        return paramItem;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<BqParamItemEntity> paramItems) {
        if (!paramItemService.saveBatch(paramItems)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
    * 根据ID更新单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQParamItemButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BqParamItemEntity update(@RequestBody BqParamItemEntity paramItem) {
        return paramItemService.saveParamItem(paramItem);
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQParamItemButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<BqParamItemEntity> paramItems) {
        if (!paramItemService.updateBatchById(paramItems)) {
            throw new BQApiException("批量更新失败");
        }
        return true;
    }


    /**
    * 根据ID删除单条记录，物理删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Boolean delete(@PathVariable String id) {
        if (!paramItemService.removePhysicalById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录,物理删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public Boolean deleteBatch(@RequestBody Collection<Serializable> ids) {
        if (!paramItemService.removePhysicalBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 根据ID删除单条记录，逻辑删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (!paramItemService.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录，逻辑删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteLogicBatch", method = RequestMethod.POST)
    public Boolean deleteLogicBatch(@RequestBody Collection<Serializable> ids) {
        if (!paramItemService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQParamItemButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<BqParamItemEntity> list(BQSearchParamsGet<BqParamItemEntity> paramsGet) {
        BQSearchParams<BqParamItemEntity> params = paramsGet.toSearchParams();
        return paramItemService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQParamItemButtons.SEARCH})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BqParamItemEntity> page(BQSearchParamsGet<BqParamItemEntity> paramsGet) {
        BQSearchParams<BqParamItemEntity> params = paramsGet.toSearchParams();
        Page<BqParamItemEntity> page = paramItemService.page(params.toPage(), params.toWrapper());
        if (!BQRequestContextHolderUtils.getUserDetails().isSuperAccount()) {
            BQRequestContextHolderUtils.setSourceTenantId();
            try {
                page.getRecords().forEach(paramItemService::getRealParamItem);
            } finally {
                BQRequestContextHolderUtils.removeSourceTenantId();
            }
        }
        return page;
    }

    /**
     * 更新图片参数类型
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQParamItemButtons.EDIT})
    @BQLogMark(module = MODULE_NAME, operation = "更新图片参数")
    @RequestMapping(value = "/update/image", method = RequestMethod.POST)
    public Object upload(
            @RequestParam("fileList") MultipartFile[] fileList,
            @RequestParam("id") int id
    ) {
        BqParamItemEntity paramItem = paramItemService.getById(id);
        if (paramItem == null) throw new BQApiException("参数不存在");
        ArrayList<String> imagePaths = getImagePathsByFiles(fileList);
        paramItem.getParamValue().putPOJO("fileList", imagePaths);
        return paramItemService.saveParamItem(paramItem);
    }
}
