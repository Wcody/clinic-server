/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月9日
*/

package com.qkplm.clinic.clinicserver.service;

import com.qkplm.clinic.clinicserver.dtos.DiagnosisDictListDto;
import com.qkplm.clinic.clinicserver.dtos.DiseaseDetailNewDto;
import com.qkplm.clinic.clinicserver.entity.TbDiagnosisDictEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;

/**
* @author Wcke
* @description <p>诊断字典表 服务类</p>
* @datetime 2026-4-9 11:43
*/
public interface ITbDiagnosisDictService extends IBaqiService<TbDiagnosisDictEntity> {

    /**
     * 获取疾病字典列表(包含疾病、首字母、专科)
     * @return 疾病字典列表DTO
     */
    DiagnosisDictListDto getDictList();

    /**
     * 获取疾病详情(新格式)
     * @param id 疾病ID
     * @return 疾病详情DTO
     */
    DiseaseDetailNewDto getDetail(Long id);
}
