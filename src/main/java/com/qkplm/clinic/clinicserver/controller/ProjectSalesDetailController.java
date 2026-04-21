/*
* 版权声明 Copyright (c) 2026.
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月20日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.qkplm.clinic.clinicserver.mapper.ProjectSalesStatsMapper;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wcke
 * @description 项目销售明细 前端控制器
 * @datetime 2026-4-20
 */
@RestController
@RequestMapping("/ams/api/v1/analysis/project")
public class ProjectSalesDetailController {

    private static final String MODULE_NAME = "项目销售明细";
    private static final String TAG_NAME = "projectSalesDetail";

    private final ProjectSalesStatsMapper projectSalesStatsMapper;

    public ProjectSalesDetailController(ProjectSalesStatsMapper projectSalesStatsMapper) {
        this.projectSalesStatsMapper = projectSalesStatsMapper;
    }

    /**
     * 项目销售明细列表（分页）
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "明细列表查询")
    @GetMapping("/detail/list")
    public Map<String, Object> list(
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String doctor,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {

        long offset = (long) (currentPage - 1) * pageSize;
        List<Map<String, Object>> list = projectSalesStatsMapper.selectProjectSalesDetail(
                projectName, doctor, startTime, endTime, offset, pageSize);
        Long total = projectSalesStatsMapper.countProjectSalesDetail(projectName, doctor, startTime, endTime);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total == null ? 0 : total);
        result.put("currentPage", currentPage);
        result.put("pageSize", pageSize);
        return result;
    }

    /**
     * 项目销售统计（按项目聚合）
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "统计列表查询")
    @GetMapping("/stat/list")
    public Map<String, Object> statList(
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {

        long offset = (long) (currentPage - 1) * pageSize;
        List<Map<String, Object>> list = projectSalesStatsMapper.selectProjectSalesStat(
                projectName, startTime, endTime, offset, pageSize);
        Long total = projectSalesStatsMapper.countProjectSalesStat(projectName, startTime, endTime);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total == null ? 0 : total);
        result.put("currentPage", currentPage);
        result.put("pageSize", pageSize);
        return result;
    }

    /**
     * 项目销售统计汇总
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "统计汇总查询")
    @GetMapping("/stat/summary")
    public Map<String, Object> statSummary(
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Map<String, Object> data = projectSalesStatsMapper.selectProjectSalesStatSummary(projectName, startTime, endTime);
        return data == null ? new HashMap<>() : data;
    }
}