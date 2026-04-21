/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月20日
*/
package com.qkplm.clinic.clinicserver.controller;

import com.qkplm.clinic.clinicserver.service.IHomeDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Wcke
 * @description 首页Dashboard统计 前端控制器
 */
@RestController
@RequestMapping("/ams/api/v1/home/dashboard")
public class HomeDashboardController {

    private static final String MODULE_NAME = "首页Dashboard统计";
    private static final String TAG_NAME = "homeDashboard";

    private final IHomeDashboardService homeDashboardService;

    public HomeDashboardController(IHomeDashboardService homeDashboardService) {
        this.homeDashboardService = homeDashboardService;
    }

    /**
     * 首页汇总数据（诊所维度）
     */
    @GetMapping("/summary")
    public Map<String, Object> summary() {
        return homeDashboardService.getSummary();
    }

    /**
     * 我的数据（个人维度）
     */
    @GetMapping("/my")
    public Map<String, Object> my() {
        return homeDashboardService.getMySummary();
    }

    /**
     * 就诊趋势数据
     */
    @GetMapping("/visit/trend")
    public Map<String, Object> visitTrend() {
        return homeDashboardService.getVisitTrend();
    }

    /**
     * 接诊数量排行
     */
    @GetMapping("/rank/visits")
    public Map<String, Object> visitRank() {
        return homeDashboardService.getVisitRank();
    }

    /**
     * 收费金额排行
     */
    @GetMapping("/rank/fees")
    public Map<String, Object> feeRank() {
        return homeDashboardService.getFeeRank();
    }

    /**
     * 最新动态
     */
    @GetMapping("/events")
    public Map<String, Object> events() {
        return homeDashboardService.getRecentEvents();
    }
}
