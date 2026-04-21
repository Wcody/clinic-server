/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月18日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.BqDiagnosisDictEntity;
import com.qkplm.clinic.clinicserver.mapper.BqDiagnosisDictMapper;
import com.qkplm.clinic.clinicserver.service.IBqDiagnosisDictService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import com.qkplm.clinic.libcommon.utils.BqPinyinUtils;

import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

/**
 * @author Wcke
 * @description
 *              <p>
 *              诊断字典表 服务接口类
 *              </p>
 * @datetime 2026-4-18 0:54
 */
@Service
public class BqDiagnosisDictServiceImpl extends BaqiServiceImpl<BqDiagnosisDictMapper, BqDiagnosisDictEntity>
        implements IBqDiagnosisDictService {
    @Override
    public boolean save(BqDiagnosisDictEntity entity) {
        if (StringUtils.hasText(entity.getDiagnosisName())) {
            entity.setPinyin(BqPinyinUtils.getAllFirstLetters(entity.getDiagnosisName()));
        }
        return super.save(entity);
    }

    @Override
    public boolean updateById(BqDiagnosisDictEntity entity) {
        if (StringUtils.hasText(entity.getDiagnosisName())) {
            entity.setPinyin(BqPinyinUtils.getAllFirstLetters(entity.getDiagnosisName()));
        }
        return super.updateById(entity);
    }
}
