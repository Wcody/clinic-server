/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.BqMedicalRecordTemplateEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

/**
* @author Wcke
* @description <p>病历模板表 服务类</p>
* @datetime 2026-4-14 12:56
*/
public interface IBqMedicalRecordTemplateService extends IBaqiService<BqMedicalRecordTemplateEntity> {

    /**
     * 获取树形结构数据
     * @return 树形结构列表
     */
    java.util.List<java.util.Map<String, Object>> getTreeData();
}
