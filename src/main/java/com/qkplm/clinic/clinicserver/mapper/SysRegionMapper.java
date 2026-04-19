/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月13日
*/

package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author Wcke
* @description <p>地区相关查询映射器</p>
* @datetime 2026-4-13
*/
@Mapper
public interface SysRegionMapper {

    /**
     * 查询所有省份列表
     */
    @Select("SELECT id, name FROM sys_province ORDER BY id ASC")
    List<Map<String, Object>> selectAllProvinces();

    /**
     * 根据省份ID查询城市列表
     */
    @Select("SELECT id, name FROM sys_city WHERE provinceId = #{provinceId} ORDER BY id ASC")
    List<Map<String, Object>> selectCitiesByProvinceId(Integer provinceId);

    /**
     * 根据城市ID查询区县/街道列表
     */
    @Select("SELECT id, name FROM sys_district WHERE cityId = #{cityId} ORDER BY id ASC")
    List<Map<String, Object>> selectDistrictsByCityId(Integer cityId);
}
