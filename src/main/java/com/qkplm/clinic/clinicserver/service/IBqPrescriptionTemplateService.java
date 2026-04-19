/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.entity.BqPrescriptionTemplateEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

import java.util.List;
import java.util.Map;

/**
* @author Wcke
* @description <p>处方模板主表 服务类</p>
* @datetime 2026-4-14 12:56
*/
public interface IBqPrescriptionTemplateService extends IBaqiService<BqPrescriptionTemplateEntity> {

    /**
     * 获取树形结构数据
     * @return 树形结构列表
     */
    List<Map<String, Object>> getTreeData();
}
