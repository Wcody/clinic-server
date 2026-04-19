/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月15日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.qkplm.clinic.clinicserver.mapper.SysRegionMapper;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Wcke
 * @description <p>省市区地区数据 前端控制器</p>
 * @datetime 2026-4-15
 */
@RestController
@RequestMapping("/ams/api/v1/region")
public class SysRegionController {

    private final static String MODULE_NAME = "省市区地区";
    private final static String TAG_NAME = "region";

    private final SysRegionMapper regionMapper;

    public SysRegionController(SysRegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    /**
     * 获取所有省份列表
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询省份")
    @RequestMapping(value = "/provinces", method = RequestMethod.GET)
    public List<Map<String, Object>> getProvinces() {
        return regionMapper.selectAllProvinces();
    }

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceId 省份ID
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询城市")
    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public List<Map<String, Object>> getCities(@RequestParam Integer provinceId) {
        if (provinceId == null) {
            return Collections.emptyList();
        }
        return regionMapper.selectCitiesByProvinceId(provinceId);
    }

    /**
     * 根据城市ID获取区县/街道列表
     *
     * @param cityId 城市ID
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询区县")
    @RequestMapping(value = "/districts", method = RequestMethod.GET)
    public List<Map<String, Object>> getDistricts(@RequestParam Integer cityId) {
        if (cityId == null) {
            return Collections.emptyList();
        }
        return regionMapper.selectDistrictsByCityId(cityId);
    }
}
