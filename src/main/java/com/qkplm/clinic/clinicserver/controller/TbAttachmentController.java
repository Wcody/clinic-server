/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */
package com.qkplm.clinic.clinicserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.qkplm.clinic.clinicserver.constant.buttons.BQFCMgrCMButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQFCMgrList1Buttons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQMCMgrCMButtons;
import com.qkplm.clinic.clinicserver.constant.buttons.BQUserButtons;
import com.qkplm.clinic.clinicserver.dtos.BQAttachmentOrderDto;
import com.qkplm.clinic.clinicserver.entity.TbCollectorEntity;
import com.qkplm.clinic.clinicserver.service.ITbAttachmentService;
import com.qkplm.clinic.clinicserver.entity.TbAttachmentEntity;
import com.qkplm.clinic.clinicserver.service.ITbCollectorService;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Wcke
 * @description <p>病案附件关联表 前端控制器</p>
 * @datetime 2024-9-21 9:1
 */
@Slf4j
@RestController
@RequestMapping("/ams/api/v1/attachment")
public class TbAttachmentController {
    private final static String MODULE_NAME = "病案附件关联表";
    private final static String TAG_NAME = "attachment";
    private final ITbAttachmentService attachmentService;
    private final ITbCollectorService collectorService;

    public TbAttachmentController(ITbAttachmentService attachmentService, ITbCollectorService collectorService) {
        this.attachmentService = attachmentService;
        this.collectorService = collectorService;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public TbAttachmentEntity get(@PathVariable String id) {
        TbAttachmentEntity attachment = attachmentService.getById(id);
        if (attachment == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return attachment;
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public TbAttachmentEntity save(@RequestBody TbAttachmentEntity attachment) {
        attachment.setEid(null);
        if (!attachmentService.save(attachment)) {
            throw new BQApiException("新增失败");
        }
        return attachment;
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<TbAttachmentEntity> attachments) {
        if (!attachmentService.saveBatch(attachments)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
    * 根据ID更新单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public TbAttachmentEntity update(@RequestBody TbAttachmentEntity attachment) {
        if (!attachmentService.updateById(attachment)) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return attachment;
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<TbAttachmentEntity> attachments) {
        if (!attachmentService.updateBatchById(attachments)) {
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
        if (!attachmentService.removePhysicalById(id)) {
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
        if (!attachmentService.removePhysicalBatchByIds(ids)) {
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
        if (!attachmentService.removeById(id)) {
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
        if (!attachmentService.removeBatchByIds(ids)) {
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
    public Iterable<TbAttachmentEntity> list(BQSearchParamsGet<TbAttachmentEntity> paramsGet) {
        BQSearchParams<TbAttachmentEntity> params = paramsGet.toSearchParams();
        return attachmentService.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<TbAttachmentEntity> page(BQSearchParamsGet<TbAttachmentEntity> paramsGet) {
        BQSearchParams<TbAttachmentEntity> params = paramsGet.toSearchParams();
        return attachmentService.page(params.toPage(), params.toWrapper());
    }

    /**
     * 上传案件关联附件
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.UPLOAD, BQFCMgrList1Buttons.UPLOAD, BQMCMgrCMButtons.UPLOAD})
    @BQLogMark(module = MODULE_NAME, operation = "上传案件关联附件")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object upload(
            @RequestParam("fileList") MultipartFile[] fileList,
            @RequestParam("recordId") String recordId,
            @RequestParam("kindId") String kindId,
            @RequestParam("uploadType") int uploadType,
            @RequestParam("initKind") int initKind
    ) {
        return attachmentService.saveAttachment(fileList, recordId, kindId, uploadType, initKind);
    }

    /**
     * 上传附件进行进行ocr自动分类和信息提取
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.UPLOAD, BQFCMgrList1Buttons.UPLOAD, BQMCMgrCMButtons.UPLOAD})
    @BQLogMark(module = MODULE_NAME, operation = "上传OCR自动分类和信息提取")
    @RequestMapping(value = "/upload4ocr", method = RequestMethod.POST)
    public Object upload4ocr(
            @RequestParam("fileList") MultipartFile[] fileList
    ) {
        return attachmentService.upload4ocr(fileList);
    }

    /**
     * 禁用、启用案件关联附件
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQUserButtons.SET_STATUS})
    @BQLogMark(module = MODULE_NAME, operation = "启用禁用")
    @RequestMapping(value = "/setStatus/{eid}/{status}", method = RequestMethod.POST)
    public Object setStatus(@PathVariable String eid, @PathVariable Boolean status) {
        return attachmentService.setStatus(eid, status);
    }

    /**
     * 更新附件分类和排序值
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/updateOrderValue", method = RequestMethod.POST)
    public Object updateAttachmentOrderValue(@RequestBody BQAttachmentOrderDto body) {
        return attachmentService.updateAttachmentOrderValue(body.getKindId(), body.getList());
    }

    /**
     * 根据recordId废止所有关联附件
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "全部废止")
    @RequestMapping(value = "/abolishByRecordId/{r}/{v}", method = RequestMethod.POST)
    public Object abolishByRecordId(@PathVariable String r, @PathVariable Boolean v) {
        return attachmentService.abolishByRecordId(r, v);
    }

    /**
     * 根据recordId删除所有关联附件
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "全部删除")
    @RequestMapping(value = "/deleteByRecordId/{r}", method = RequestMethod.POST)
    public Object deleteByRecordId(@PathVariable String r) {
        return attachmentService.deleteByRecordId(r);
    }

    /**
     * 测试连通性
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {BQFCMgrCMButtons.UPLOAD, BQFCMgrList1Buttons.UPLOAD, BQMCMgrCMButtons.UPLOAD})
    @BQLogMark(module = MODULE_NAME, operation = "采集器测试连通性")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Object test(@RequestBody TbCollectorEntity collector) {
        String collectorName = collector.getCollectorName();
        String deviceId = collector.getDeviceId();
        if (StringUtils.isBlank(collectorName) || StringUtils.isBlank(deviceId)) {
            throw new BQApiException("采集器名称和设备ID不能为空");
        }
        if (Objects.nonNull(collector.getEid()) && collector.getEid().length() < 20) {
            collector.setEid(null);
        }
        TbCollectorEntity collectorEntity = collectorService.getByNameAndDeviceId(collectorName, deviceId);
        if (Objects.isNull(collectorEntity)) {
            collector.setStatus(false);
            collector.setEid(null);
        } else {
            collector.setEid(collectorEntity.getEid());
            collector.setStatus(collectorEntity.getStatus());
        }
        collector.setRemoteIp(BQRequestContextHolderUtils.getRealRemoteIP());
        collector.setRemotePort(BQRequestContextHolderUtils.getRealRemotePort());
        collector.setLastTime(LocalDateTime.now());

        collectorService.saveOrUpdate(collector);
        log.info("测试连通性, {}", BQJacksonUtils.toJson(collector));

        if (!collector.getStatus()) {
            throw new BQApiException("采集器已被禁用无法访问，请联系系统管理员");
        }

        return collector;
    }

    /**
     * 根据recordId和kindId废止关联附件
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分类下废止")
    @RequestMapping(value = "/abolishByKindId/{r}/{k}/{v}", method = RequestMethod.POST)
    public Object abolishByKindId(@PathVariable String r, @PathVariable String k, @PathVariable Boolean v) {
        return attachmentService.abolishByKindId(r, k, v);
    }

    /**
     * 根据recordId和kindId删除关联附件
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分类下删除")
    @RequestMapping(value = "/deleteByKindId/{r}/{k}", method = RequestMethod.POST)
    public Object deleteByKindId(@PathVariable String r, @PathVariable String k) {
        return attachmentService.deleteByKindId(r, k);
    }

    /**
     * 废止文件对象
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "废止")
    @RequestMapping(value = "/abolishByAttachmentId/{e}/{v}", method = RequestMethod.POST)
    public Object abolishByAttachmentId(@PathVariable String e, @PathVariable Boolean v) {
        return attachmentService.abolishByAttachmentId(e, v);
    }
}
