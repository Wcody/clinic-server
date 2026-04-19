/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.qkplm.clinic.clinicserver.mapper.WorkloadStatsMapper;
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
 * @description 工作量统计 前端控制器
 */
@RestController
@RequestMapping("/ams/api/v1/workload/stats")
public class WorkloadStatsController {

    private static final String MODULE_NAME = "工作量统计";
    private static final String TAG_NAME = "workloadStats";

    private final WorkloadStatsMapper workloadStatsMapper;

    public WorkloadStatsController(WorkloadStatsMapper workloadStatsMapper) {
        this.workloadStatsMapper = workloadStatsMapper;
    }

    /**
     * 工作量统计列表（按医生聚合）
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "工作量列表查询")
    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String doctor) {

        List<Map<String, Object>> list =
                workloadStatsMapper.selectWorkloadByDoctor(startTime, endTime, doctor);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", list.size());
        return result;
    }

    /**
     * 汇总卡片数据
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "工作量汇总查询")
    @GetMapping("/summary")
    public Map<String, Object> summary(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String doctor) {

        Map<String, Object> data =
                workloadStatsMapper.selectWorkloadSummary(startTime, endTime, doctor);
        return data == null ? new HashMap<>() : data;
    }
}
