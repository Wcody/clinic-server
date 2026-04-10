/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.TbKindTemplateEntity;
import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.io.File;
import java.util.List;

/**
* @author Wcke
* @description <p>分类模板 服务类</p>
* @datetime 2024-7-12 9:43
*/
public interface ITbKindTemplateService extends IBaqiService<TbKindTemplateEntity> {
    //获取所有符合条件的数据
    List<TbKindTemplateEntity> getAllList();

    //ocr处理
    void handleOcr(File imageFile);

    int handleOcr(File imageFile, List<TbKindTemplateEntity> list);

    //ocr处理
    void handleOcr(String imagePath);

    int handleOcr(String imagePath, List<TbKindTemplateEntity> list);

    boolean handleOcr(String imagePath, TbKindTemplateEntity entity);

    boolean handleOcr(File imageFile, TbKindTemplateEntity entity);

    // ocr获取数据
    TbRecordEntity handleOcr4Record(File imageFile, TbKindTemplateEntity entity);
}
