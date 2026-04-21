/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月13日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qkplm.clinic.clinicserver.entity.BqPatientEntity;
import com.qkplm.clinic.clinicserver.mapper.BqPatientMapper;
import com.qkplm.clinic.clinicserver.service.IBqPatientService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import com.qkplm.clinic.libcommon.utils.BQDateUtils;
import com.qkplm.clinic.libcommon.utils.BqPinyinUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * @author Wcke
 * @description
 *              <p>
 *              患者信息表 服务接口类
 *              </p>
 * @datetime 2026-4-13 11:55
 */
@Service
public class BqPatientServiceImpl extends BaqiServiceImpl<BqPatientMapper, BqPatientEntity>
        implements IBqPatientService {

    @Override
    public Iterable<BqPatientEntity> searchByKeyword(String keyword) {
        // 如果关键字为空，返回空列表
        if (!StringUtils.hasText(keyword)) {
            return Collections.emptyList();
        }
        // 构建查询条件：姓名、手机号、身份证号任意一个包含关键字
        LambdaQueryWrapper<BqPatientEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.like(BqPatientEntity::getName, keyword)
                .or()
                .like(BqPatientEntity::getMobile, keyword)
                .or()
                .like(BqPatientEntity::getIdCard, keyword))
                .eq(BqPatientEntity::getDeleted, false)
                .orderByDesc(BqPatientEntity::getCreatedTime);

        return this.list(wrapper);
    }

    @Override
    public boolean save(BqPatientEntity entity) {
        // 设置档案号
        entity.setArchiveNo(BQDateUtils.getCurrentDateMillsNoSepShortYear());
        if (StringUtils.hasText(entity.getName())) {
            entity.setPinyin(BqPinyinUtils.getAllFirstLetters(entity.getName()));
        }

        return super.save(entity);
    }

    @Override
    public boolean updateById(BqPatientEntity entity) {
        if (StringUtils.hasText(entity.getName())) {
            entity.setPinyin(BqPinyinUtils.getAllFirstLetters(entity.getName()));
        }

        return super.updateById(entity);
    }
}
