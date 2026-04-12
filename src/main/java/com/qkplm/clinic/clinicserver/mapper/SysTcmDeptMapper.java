/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月11日
*/

package com.qkplm.clinic.clinicserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author Wcke
* @description <p>中医科室相关查询映射器</p>
* @datetime 2026-4-11
*/
@Mapper
public interface SysTcmDeptMapper {

    /**
     * 查询所有科室,按sort排序
     */
    @Select("SELECT id, deptName FROM sys_tcm_dept ORDER BY sort ASC")
    List<Map<String, Object>> selectAllDepts();
}
