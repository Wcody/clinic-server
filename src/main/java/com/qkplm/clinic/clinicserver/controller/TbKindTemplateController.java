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
import com.qkplm.clinic.clinicserver.constant.buttons.BQMKindKeysButtons;
import com.qkplm.clinic.clinicserver.service.ITbKindTemplateService;
import com.qkplm.clinic.clinicserver.entity.TbKindTemplateEntity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;
import com.qkplm.clinic.libcommon.utils.BQIDUtils;
import com.qkplm.clinic.libcommon.utils.BQUploadHandlerUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Wcke
 * @description <p>分类模板 前端控制器</p>
 * @datetime 2024-7-12 9:43
 */
@RestController
@RequestMapping("/ams/api/v1/kind/template")
public class TbKindTemplateController {
    private final static String MODULE_NAME = "分类模板";
    private final static String TAG_NAME = "kindTemplate";
    private final ITbKindTemplateService kindTemplateService;

    public TbKindTemplateController(ITbKindTemplateService kindTemplateService) {
        this.kindTemplateService = kindTemplateService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbKindTemplateEntity get(@PathVariable String id) {
        TbKindTemplateEntity kindTemplate = kindTemplateService.getById(id);
        if (kindTemplate == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return kindTemplate;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbKindTemplateEntity save(@RequestBody TbKindTemplateEntity kindTemplate) {
        kindTemplate.setEid(null);
        if (!kindTemplateService.save(kindTemplate)) {
            throw new BQApiException("新增失败");
        }
        return kindTemplate;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbKindTemplateEntity> kindTemplates) {
        if (!kindTemplateService.saveBatch(kindTemplates)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
     * 上传单条记录
     */
    @BQAuthMark(tag = TAG_NAME, buttons = { BQMKindKeysButtons.UPLOAD_TEMP })
    @BQLogMark(module = MODULE_NAME, operation = "上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public TbKindTemplateEntity upload(@RequestParam("file") MultipartFile file, String kindId) throws IOException {
        // 保持数据
        String filename = file.getOriginalFilename();
        TbKindTemplateEntity kindTemplate = new TbKindTemplateEntity();
        kindTemplate.setKindId(kindId);
        String suffix = Objects.requireNonNull(filename).substring(filename.lastIndexOf("."));
        filename = BQIDUtils.uuid() + suffix;
        kindTemplate.setName(filename);
        filename = BQUploadHandlerUtils.getSubPath(filename);
        kindTemplate.setImgPath(filename);
        kindTemplate.setNineType(file.getContentType());
        kindTemplate.setImgSize(file.getSize());
        File imageFile = BQUploadHandlerUtils.getTemplateFileBySubPath(filename);
        // 保存文件
        file.transferTo(imageFile);
        BufferedImage read = ImageIO.read(imageFile);
        kindTemplate.setImgWidth(read.getWidth());
        kindTemplate.setImgHeight(read.getHeight());
        if (!kindTemplateService.save(kindTemplate)) throw new BQApiException("上传失败");
        return kindTemplate;
    }

    /**
    * 根据ID更新单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public TbKindTemplateEntity update(@RequestBody TbKindTemplateEntity kindTemplate) {
        if (!kindTemplateService.updateById(kindTemplate)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return kindTemplate;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbKindTemplateEntity> kindTemplates) {
        if (!kindTemplateService.updateBatchById(kindTemplates)) {
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
        if (!kindTemplateService.removePhysicalById(id)) {
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
        if (!kindTemplateService.removePhysicalBatchByIds(ids)) {
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
        if (!kindTemplateService.removeById(id)) {
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
        if (!kindTemplateService.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<TbKindTemplateEntity> list(BQSearchParamsGet<TbKindTemplateEntity> paramsGet) {
        BQSearchParams<TbKindTemplateEntity> params = paramsGet.toSearchParams();
        return kindTemplateService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbKindTemplateEntity> page(BQSearchParamsGet<TbKindTemplateEntity> paramsGet) {
        BQSearchParams<TbKindTemplateEntity> params = paramsGet.toSearchParams();
        return kindTemplateService.page(params.toPage(), params.toWrapper());
    }
}
