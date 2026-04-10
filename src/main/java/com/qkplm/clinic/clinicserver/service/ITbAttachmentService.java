/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import org.springframework.web.multipart.MultipartFile;
import com.qkplm.clinic.clinicserver.entity.TbAttachmentEntity;
import com.qkplm.clinic.clinicserver.entity.TbKindTemplateEntity;
import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.util.List;

/**
* @author Wcke
* @description <p>病案附件关联表 服务类</p>
* @datetime 2024-9-21 9:1
*/
public interface ITbAttachmentService extends IBaqiService<TbAttachmentEntity> {
    Object setStatus(String eid, Boolean status);
    Object saveAttachment(MultipartFile[] fileList, String recordId, String kindId, int uploadType,  int initKind);
    Object upload4ocr(MultipartFile[] fileList);
    Object updateAttachmentOrderValue(String kindId, String[] attachmentIds);
    Object abolishByRecordId(String recordId, boolean value);
    Object deleteByRecordId(String recordId);
    Object abolishByKindId(String recordId, String kindId, boolean value);
    Object deleteByKindId(String recordId, String kindId);
    Object abolishByAttachmentId(String attachmentId, boolean value);
    // 完全相同忽略，重复则更新图片地址，否则新增
    Object handleSaveOrUpdate(TbRecordEntity recordEntity, TbKindTemplateEntity templateEntity, TbAttachmentEntity attachmentEntity);
    TbAttachmentEntity getBy(String recordId, String kindId, String fileHash);
    List<TbAttachmentEntity> getListBy(String recordId, String kindId);
    List<String> getKindIdsByRecordId(String recordId);
}
