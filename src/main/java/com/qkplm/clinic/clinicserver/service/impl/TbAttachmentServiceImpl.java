/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qkplm.clinic.libcommon.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.qkplm.clinic.clinicserver.entity.TbAttachmentEntity;
import com.qkplm.clinic.clinicserver.entity.TbKindEntity;
import com.qkplm.clinic.clinicserver.entity.TbKindTemplateEntity;
import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;
import com.qkplm.clinic.clinicserver.mapper.TbAttachmentMapper;
import com.qkplm.clinic.clinicserver.service.ITbAttachmentService;
import com.qkplm.clinic.clinicserver.service.ITbKindService;
import com.qkplm.clinic.clinicserver.service.ITbKindTemplateService;
import com.qkplm.clinic.clinicserver.service.ITbRecordService;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.utils.*;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* @author Wcke
* @description <p>病案附件关联表 服务接口类</p>
* @datetime 2024-9-21 9:1
*/
@Slf4j
@Service
public class TbAttachmentServiceImpl extends BaqiServiceImpl<TbAttachmentMapper, TbAttachmentEntity> implements ITbAttachmentService {
    private final ITbKindTemplateService tbKindTemplateService;
    private final ITbRecordService tbRecordService;
    private final ITbKindService tbKindService;

    public static final int UPLOAD_TYPE_PDF = 1;
    public static final int UPLOAD_TYPE_IMG = 2;

    public TbAttachmentServiceImpl(ITbKindTemplateService tbKindTemplateService, ITbRecordService tbRecordService, @Lazy ITbKindService tbKindService) {
        this.tbKindTemplateService = tbKindTemplateService;
        this.tbRecordService = tbRecordService;
        this.tbKindService = tbKindService;
    }

    private TbAttachmentEntity getByFile(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        TbAttachmentEntity attachmentEntity = new TbAttachmentEntity();
        attachmentEntity.setFileCode(filename);
        attachmentEntity.setFileName(filename);
        attachmentEntity.setFileSize(file.getSize());
        attachmentEntity.setMimeType(file.getContentType());
        String suffix = Objects.requireNonNull(filename).substring(filename.lastIndexOf("."));
        filename = BQIDUtils.uuid() + suffix;
        filename = BQUploadHandlerUtils.getSubPath(filename);
        attachmentEntity.setFilePath(filename);
        File subFile = BQUploadHandlerUtils.getMedicalFileBySubPath(filename);
        file.transferTo(subFile);
        attachmentEntity.setFileHash(BQFileUtils.getFileMD5(subFile));
        return attachmentEntity;
    }

    @Override
    public Object setStatus(String eid, Boolean status) {
        TbAttachmentEntity attachment = getById(eid);
        if (Objects.nonNull(attachment)) {
            attachment.setStatus(status);
            return updateById(attachment);
        }
        throw new BQApiException("目标不存在");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object saveAttachment(MultipartFile[] fileList, String recordId, String kindId, int uploadType, int initKind) {
        try {
            for (MultipartFile file : fileList) {
                if (uploadType == UPLOAD_TYPE_IMG) {
                    //图片文件上传处理
                    TbAttachmentEntity attachmentEntity = getByFile(file);
                    attachmentEntity.setKindId(kindId);
                    attachmentEntity.setRecordId(recordId);
                    if (!save(attachmentEntity)) throw new BQApiException("上传失败");
                } else {
                    // PDF文件上传处理
                    // 拆成一页一张图片的形式

                    throw new BQApiException("暂不支持上传PDF文件");
                }

            }
        } catch (Exception e) {
            log.error("上传失败", e);
            throw new BQApiException("上传失败");
        }

        return true;
    }

    private void saveEntityByImageFile(File imageFile, TbAttachmentEntity attachmentEntity, List<TbKindTemplateEntity> allList) {
        int index = tbKindTemplateService.handleOcr(imageFile, allList);
        log.info("返回的索引: {}", index);
        // 获取分类ID
        if (index != -1) {
            TbKindTemplateEntity entity = allList.get(index);
            attachmentEntity.setKindId(entity.getKindId());
            log.info("返回的分类ID: {}", entity.getKindId());
            TbKindEntity kindEntity = tbKindService.getById(entity.getKindId());
            log.info("获取到分类对象: {}", BQJacksonUtils.toJson(kindEntity));

            // 获取记录
            TbRecordEntity recordEntity = tbKindTemplateService.handleOcr4Record(imageFile, entity);
            log.info("返回的档案记录: {}", BQJacksonUtils.toJson(recordEntity));
            if (Objects.nonNull(recordEntity)) {
                // 0档案、1住院、2门急诊
                recordEntity.setRecordKind(kindEntity.getKind());
                // 检查病案号
                String recordCode = recordEntity.getRecordCode();
                // 病案号存在
                if (Objects.nonNull(recordCode)) {
                    TbRecordEntity record = tbRecordService.getByRecordCode(recordCode);
                    if (Objects.nonNull(record)) {
                        record.assignFrom(recordEntity);
                        log.info("病案记录存在，更新: {}", BQJacksonUtils.toJson(record));
                        // 更新病案字段
                        tbRecordService.updateById(record);
                        attachmentEntity.setRecordId(record.getEid());
                    } else {
                        // 新增病案记录
                        tbRecordService.save(recordEntity);
                        log.info("病案记录不存在，新增: {}", BQJacksonUtils.toJson(recordEntity));
                        attachmentEntity.setRecordId(recordEntity.getEid());
                    }

                    // 判断附件是否存在，recordId，kindId，fileHash相同则忽略，否则根据设置的唯一性字段校验，重复仅替换图片地址
                    handleSaveOrUpdate(recordEntity, entity, attachmentEntity);
                    // this.save(attachmentEntity);
                }

                log.info("上传成功: {}", BQJacksonUtils.toJson(attachmentEntity));
            } else {
                log.info("返回的档案记录为空, 忽略文件：{}", imageFile.getAbsolutePath());
            }
        }
    }

    @Override
    public Object upload4ocr(MultipartFile[] fileList) {
        try {
            List<TbKindTemplateEntity> allList = tbKindTemplateService.getAllList();
            for (MultipartFile file : fileList) {
                // 这里判断是否PDF文件，分解处理
                if (Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().endsWith(".pdf")) {
                    // 处理PDF文件, 创建uuid文件夹
                    String pdfFileRootPath = BQUploadHandlerUtils.getPdfsPath() + File.separator + BQIDUtils.uuid();
                    String pdfImagesPath = pdfFileRootPath + File.separator + "images";
                    if (new File(pdfImagesPath).mkdirs()) {
                        log.info("创建文件夹: {}", pdfImagesPath);
                    }
                    try {
                        String pdfFilePath = pdfFileRootPath + File.separator + file.getOriginalFilename();
                        file.transferTo(new File(pdfFilePath));
                        ArrayList<File> imageFiles = BQPDFUtils.extractPDF2Image(pdfFilePath, pdfImagesPath);
                        for (File imageFile : imageFiles) {
                            if (!imageFile.exists()) {
                                log.warn("PDF{}提取了{}个页面图片，该图片{}不存在", pdfFilePath, imageFiles.size(), imageFile.getAbsolutePath());
                                continue;
                            }
                            TbAttachmentEntity attachmentEntity = new TbAttachmentEntity();
                            String filename = imageFile.getName();
                            attachmentEntity.setFileCode(filename);
                            attachmentEntity.setFileName(filename);
                            attachmentEntity.setFileSize(imageFile.length());
                            attachmentEntity.setMimeType("image/png");
                            String suffix = Objects.requireNonNull(filename).substring(filename.lastIndexOf("."));
                            filename = BQIDUtils.uuid() + suffix;
                            filename = BQUploadHandlerUtils.getSubPath(filename);
                            attachmentEntity.setFilePath(filename);
                            File subFile = BQUploadHandlerUtils.getMedicalFileBySubPath(filename);
                            Files.copy(imageFile.toPath(), subFile.toPath());
                            attachmentEntity.setFileHash(BQFileUtils.getFileMD5(subFile));
                            // 保存附件
                            saveEntityByImageFile(subFile, attachmentEntity, allList);
                        }
                    } catch (Exception e) {
                        log.error("PDF文件处理失败", e);
                        throw new BQApiException("PDF文件处理失败");
                    }
                } else {
                    TbAttachmentEntity attachmentEntity = getByFile(file);
                    File subFile = BQUploadHandlerUtils.getMedicalFileBySubPath(attachmentEntity.getFilePath());
                    saveEntityByImageFile(subFile, attachmentEntity, allList);
                }
            }
        } catch (Exception e) {
            log.error("上传失败", e);
            throw new BQApiException("上传失败");
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object updateAttachmentOrderValue(String kindId, String[] attachmentIds) {
        List<TbAttachmentEntity> list = new ArrayList<>();
        for (int i = 0; i < attachmentIds.length; i++) {
            TbAttachmentEntity attachment = new TbAttachmentEntity();
            attachment.setOrderValue(i);
            attachment.setEid(attachmentIds[i]);
            attachment.setKindId(kindId);
            list.add(attachment);
        }
        return updateBatchById(list);
    }

    @Override
    public Object abolishByRecordId(String recordId, boolean value) {
        LambdaUpdateWrapper<TbAttachmentEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(TbAttachmentEntity::getStatus, value).eq(TbAttachmentEntity::getRecordId, recordId);
        return update(updateWrapper);
    }

    @Override
    public Object deleteByRecordId(String recordId) {
        LambdaQueryWrapper<TbAttachmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbAttachmentEntity::getRecordId, recordId);
        return remove(queryWrapper);
    }

    @Override
    public Object abolishByKindId(String recordId, String kindId, boolean value) {
        LambdaUpdateWrapper<TbAttachmentEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(TbAttachmentEntity::getStatus, value).eq(TbAttachmentEntity::getRecordId,
                recordId).eq(TbAttachmentEntity::getKindId, kindId);
        return update(updateWrapper);
    }

    @Override
    public Object deleteByKindId(String recordId, String kindId) {
        LambdaQueryWrapper<TbAttachmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbAttachmentEntity::getRecordId, recordId).eq(TbAttachmentEntity::getKindId, kindId);
        return remove(queryWrapper);
    }

    @Override
    public Object abolishByAttachmentId(String attachmentId, boolean value) {
        LambdaUpdateWrapper<TbAttachmentEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(TbAttachmentEntity::getStatus, value).eq(TbAttachmentEntity::getEid, attachmentId);
        return update(updateWrapper);
    }

    @Override
    public Object handleSaveOrUpdate(TbRecordEntity recordEntity, TbKindTemplateEntity templateEntity, TbAttachmentEntity attachmentEntity) {
        String fileHash = attachmentEntity.getFileHash();
        String recordId = attachmentEntity.getRecordId();
        String kindId = attachmentEntity.getKindId();
        if (Objects.isNull(fileHash) || Objects.isNull(recordId) || Objects.isNull(kindId)) {
            log.error("参数不全：{}，{}，{}", fileHash, recordId, kindId);
            throw new BQApiException("参数不全");
        }

        // 先判断是否完全相同
        TbAttachmentEntity dbEntity = getBy(recordId, kindId, fileHash);
        if (Objects.nonNull(dbEntity)) {
            log.info("文档完全相同，不作入库处理：{}，{}，{}", fileHash, recordId, kindId);
            return false;
        }

        // 根据配置判断是否有唯一性校验字段
        ArrayNode arrayNode = templateEntity.getDataFields();
        if (Objects.nonNull(arrayNode) && !arrayNode.isEmpty()) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode node = arrayNode.get(i);
                if (node.isObject()) {
                    ObjectNode objectNode = (ObjectNode) node;
                    JsonNode uNode = objectNode.get("u");
                    if (Objects.nonNull(uNode) && uNode.asBoolean()) {
                        list.add(objectNode.get("enFieldName").asText());
                    }
                }
            }

            // 唯一字段不为空
            if (!list.isEmpty()) {
                List<TbAttachmentEntity> entities = getListBy(recordId, kindId);
                if (!entities.isEmpty()) {
                    // 相同病案相同分类的列表不为空
                    for (TbAttachmentEntity item : entities) {
                        File subFile = BQUploadHandlerUtils.getMedicalFileBySubPath(item.getFilePath());
                        TbRecordEntity dbOcrEntity = tbKindTemplateService.handleOcr4Record(subFile, templateEntity);

                        boolean flag = true;
                        for (String fieldName : list) {
                            Object currValue = recordEntity.get(fieldName);
                            Object ocrValue = dbOcrEntity.get(fieldName);
                            flag = Objects.equals(currValue, ocrValue);
                            if (!flag) break;
                        }

                        if (flag) {
                            log.info("文档唯一性校验相同，只更新文档路径");
                            item.setFilePath(attachmentEntity.getFilePath());
                            return updateById(item);
                        }
                    }
                }
            }
        }

        // 不满足唯一性校验条件直接入库
        log.info("不满足唯一性校验条件，直接入库");
        return save(attachmentEntity);
    }

    @Override
    public TbAttachmentEntity getBy(String recordId, String kindId, String fileHash) {
        LambdaQueryWrapper<TbAttachmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbAttachmentEntity::getRecordId, recordId).eq(TbAttachmentEntity::getKindId, kindId)
                .eq(TbAttachmentEntity::getFileHash, fileHash);
        return getOne(queryWrapper);
    }

    @Override
    public List<TbAttachmentEntity> getListBy(String recordId, String kindId) {
        LambdaQueryWrapper<TbAttachmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbAttachmentEntity::getRecordId, recordId).eq(TbAttachmentEntity::getKindId, kindId);
        return list(queryWrapper);
    }

    @Override
    public List<String> getKindIdsByRecordId(String recordId) {
        return this.getBaseMapper().getKindIdsByRecordId(recordId);
    }
}
