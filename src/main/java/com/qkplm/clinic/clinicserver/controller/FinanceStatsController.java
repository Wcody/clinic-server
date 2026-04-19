/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.qkplm.clinic.clinicserver.mapper.FinanceStatsMapper;
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
 * @description 财务统计 前端控制器
 * @datetime 2026-4-14
 */
@RestController
@RequestMapping("/ams/api/v1/finance/stats")
public class FinanceStatsController {

    private static final String MODULE_NAME = "财务统计";
    private static final String TAG_NAME = "financeStats";

    private final FinanceStatsMapper financeStatsMapper;

    public FinanceStatsController(FinanceStatsMapper financeStatsMapper) {
        this.financeStatsMapper = financeStatsMapper;
    }

    /**
     * 财务统计列表（分页）
     * @param dimension 时间维度：日/月
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param typeString 收费类型
     * @param currentPage 当前页
     * @param pageSize  每页大小
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "财务列表查询")
    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "日") String dimension,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String typeString,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "20") Integer pageSize) {

        long offset = (long) (currentPage - 1) * pageSize;
        List<Map<String, Object>> list;
        Long total;

        if ("月".equals(dimension)) {
            list = financeStatsMapper.selectStatsByMonth(startTime, endTime, typeString, offset, pageSize);
            total = financeStatsMapper.countStatsByMonth(startTime, endTime, typeString);
        } else {
            list = financeStatsMapper.selectStatsByDay(startTime, endTime, typeString, offset, pageSize);
            total = financeStatsMapper.countStatsByDay(startTime, endTime, typeString);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total == null ? 0 : total);
        result.put("currentPage", currentPage);
        result.put("pageSize", pageSize);
        return result;
    }

    /**
     * 汇总卡片数据
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "财务汇总查询")
    @GetMapping("/summary")
    public Map<String, Object> summary(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String typeString) {
        Map<String, Object> data = financeStatsMapper.selectSummary(startTime, endTime, typeString);
        return data == null ? new HashMap<>() : data;
    }

    /**
     * 折线图数据
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "财务图表查询")
    @GetMapping("/chart")
    public Map<String, Object> chart(
            @RequestParam(defaultValue = "日") String dimension,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String typeString) {

        List<Map<String, Object>> rows = "月".equals(dimension)
                ? financeStatsMapper.selectChartByMonth(startTime, endTime, typeString)
                : financeStatsMapper.selectChartByDay(startTime, endTime, typeString);

        List<String> dates = rows.stream()
                .map(r -> String.valueOf(r.get("periodDate")))
                .collect(java.util.stream.Collectors.toList());
        List<Object> amounts = rows.stream()
                .map(r -> r.get("totalAmount"))
                .collect(java.util.stream.Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("dates", dates);
        result.put("amounts", amounts);
        return result;
    }

    /**
     * 收费类型下拉选项
     */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "收费类型查询")
    @GetMapping("/types")
    public List<String> types() {
        return financeStatsMapper.selectTypeStrings();
    }
}
