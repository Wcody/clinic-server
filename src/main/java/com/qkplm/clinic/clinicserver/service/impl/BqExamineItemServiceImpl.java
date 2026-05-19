/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.BqExamineItemEntity;
import com.qkplm.clinic.clinicserver.mapper.BqExamineItemMapper;
import com.qkplm.clinic.clinicserver.service.IBqExamineItemService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import com.qkplm.clinic.libcommon.utils.BqPinyinUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author Wcke
* @description <p>检查检验项目表 服务接口类</p>
* @datetime 2026-4-14 12:56
*/
@Service
public class BqExamineItemServiceImpl extends BaqiServiceImpl<BqExamineItemMapper, BqExamineItemEntity> implements IBqExamineItemService {

    @Override
    public boolean save(BqExamineItemEntity entity) {
        if (StringUtils.hasText(entity.getName()))
            entity.setPinyin(BqPinyinUtils.getAllFirstLettersPolyphonic(entity.getName()));
        return super.save(entity);
    }

    @Override
    public boolean updateById(BqExamineItemEntity entity) {
        if (StringUtils.hasText(entity.getName()))
            entity.setPinyin(BqPinyinUtils.getAllFirstLettersPolyphonic(entity.getName()));
        return super.updateById(entity);
    }
}
