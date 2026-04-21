/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.BqDrugEntity;
import com.qkplm.clinic.clinicserver.mapper.BqDrugMapper;
import com.qkplm.clinic.clinicserver.service.IBqDrugService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import com.qkplm.clinic.libcommon.utils.BqPinyinUtils;

import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Wcke
 * @description
 *              <p>
 *              药品管理表 服务接口类
 *              </p>
 * @datetime 2026-4-14 12:56
 */
@Service
public class BqDrugServiceImpl extends BaqiServiceImpl<BqDrugMapper, BqDrugEntity> implements IBqDrugService {

    @Override
    public boolean save(BqDrugEntity entity) {
        if (StringUtils.hasText(entity.getName()))
            entity.setPinyin(BqPinyinUtils.getAllFirstLetters(entity.getName()));
        return super.save(entity);
    }

    @Override
    public boolean updateById(BqDrugEntity entity) {
        if (StringUtils.hasText(entity.getName()))
            entity.setPinyin(BqPinyinUtils.getAllFirstLetters(entity.getName()));
        return super.updateById(entity);
    }
}
