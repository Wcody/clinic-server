/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.BqAdditionalFeeEntity;
import com.qkplm.clinic.clinicserver.mapper.BqAdditionalFeeMapper;
import com.qkplm.clinic.clinicserver.service.IBqAdditionalFeeService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import com.qkplm.clinic.libcommon.utils.BqPinyinUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author Wcke
* @description <p>附加费设置表 服务接口类</p>
* @datetime 2026-4-14 12:56
*/
@Service
public class BqAdditionalFeeServiceImpl extends BaqiServiceImpl<BqAdditionalFeeMapper, BqAdditionalFeeEntity> implements IBqAdditionalFeeService {

    @Override
    public boolean save(BqAdditionalFeeEntity entity) {
        if (StringUtils.hasText(entity.getName()))
            entity.setPinyin(BqPinyinUtils.getAllFirstLettersPolyphonic(entity.getName()));
        return super.save(entity);
    }

    @Override
    public boolean updateById(BqAdditionalFeeEntity entity) {
        if (StringUtils.hasText(entity.getName()))
            entity.setPinyin(BqPinyinUtils.getAllFirstLettersPolyphonic(entity.getName()));
        return super.updateById(entity);
    }
}
