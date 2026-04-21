/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月20日
*/
package com.qkplm.clinic.clinicserver.service;

import java.util.Map;

/**
 * @author Wcke
 * @description 首页Dashboard统计 Service接口
 */
public interface IHomeDashboardService {

    /**
     * 获取首页汇总数据（诊所维度）
     */
    Map<String, Object> getSummary();

    /**
     * 获取我的数据（个人维度）
     */
    Map<String, Object> getMySummary();

    /**
     * 获取就诊趋势数据
     */
    Map<String, Object> getVisitTrend();

    /**
     * 获取接诊数量排行
     */
    Map<String, Object> getVisitRank();

    /**
     * 获取收费金额排行
     */
    Map<String, Object> getFeeRank();

    /**
     * 获取最新动态
     */
    Map<String, Object> getRecentEvents();
}
