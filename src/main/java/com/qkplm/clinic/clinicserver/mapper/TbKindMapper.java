/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Select;
import com.qkplm.clinic.clinicserver.entity.TbKindEntity;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Wcke
* @description <p>档案分类 映射器</p>
* @datetime 2024-7-12 9:43
*/
@Mapper
public interface TbKindMapper extends IBaqiMapper<TbKindEntity> {
    @Select("${sql}")
    List<TbKindEntity> listQt(String sql);
}
