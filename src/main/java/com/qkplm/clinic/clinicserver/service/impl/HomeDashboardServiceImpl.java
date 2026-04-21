/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月20日
*/
package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.mapper.HomeDashboardMapper;
import com.qkplm.clinic.clinicserver.service.IHomeDashboardService;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Wcke
 * @description 首页Dashboard统计 Service实现类
 */
@Service
public class HomeDashboardServiceImpl implements IHomeDashboardService {

    private static final String[] DAY_NAMES = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    private final HomeDashboardMapper homeDashboardMapper;

    public HomeDashboardServiceImpl(HomeDashboardMapper homeDashboardMapper) {
        this.homeDashboardMapper = homeDashboardMapper;
    }

    @Override
    public Map<String, Object> getSummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime dayStart = today.atStartOfDay();
        LocalDateTime dayEnd = today.plusDays(1).atStartOfDay();

        Integer todayTotal = homeDashboardMapper.selectTodayRegistrationTotal(dayStart, dayEnd);
        Integer myReceived = homeDashboardMapper.selectReceivedCount(dayStart, dayEnd);
        Integer myPending = homeDashboardMapper.selectPendingCount(dayStart, dayEnd);
        BigDecimal myFee = homeDashboardMapper.selectTodayFee(dayStart, dayEnd);

        Map<String, Object> result = new HashMap<>();
        result.put("todayRegistrationTotal", todayTotal != null ? todayTotal : 0);
        result.put("myReceivedCount", myReceived != null ? myReceived : 0);
        result.put("myPendingCount", myPending != null ? myPending : 0);
        result.put("myTodayFee", myFee != null ? myFee : BigDecimal.ZERO);
        return result;
    }

    @Override
    public Map<String, Object> getVisitTrend() {
        LocalDate today = LocalDate.now();
        int todayDayOfWeek = today.getDayOfWeek().getValue(); // 1=周一, 7=周日

        // 计算本周开始（上周对应的今天）
        LocalDate thisWeekStart = today.minusDays(6); // 今天减6天 = 本周第一天

        // 查询本周7天的数据（上周二 ~ 今天）
        LocalDateTime thisWeekEnd = today.plusDays(1).atStartOfDay();
        LocalDate lastWeekStart = thisWeekStart.minusDays(7); // 上上周二
        LocalDateTime lastWeekEnd = thisWeekStart.atStartOfDay(); // 上周一

        List<Map<String, Object>> thisWeekData = homeDashboardMapper.selectThisWeekVisitCount(
                thisWeekStart.atStartOfDay(), thisWeekEnd);
        List<Map<String, Object>> lastWeekData = homeDashboardMapper.selectLastWeekVisitCount(
                lastWeekStart.atStartOfDay(), lastWeekEnd);

        // 构建本周数据（周日=1，所以索引为dayOfWeek-1）
        int[] thisWeekCounts = new int[7];
        int[] lastWeekCounts = new int[7];

        for (Map<String, Object> row : thisWeekData) {
            Object dayObj = row.get("dayOfWeek");
            Object countObj = row.get("count");
            if (dayObj != null && countObj != null) {
                int day = ((Number) dayObj).intValue();
                int count = ((Number) countObj).intValue();
                if (day >= 1 && day <= 7) {
                    thisWeekCounts[day - 1] = count;
                }
            }
        }

        for (Map<String, Object> row : lastWeekData) {
            Object dayObj = row.get("dayOfWeek");
            Object countObj = row.get("count");
            if (dayObj != null && countObj != null) {
                int day = ((Number) dayObj).intValue();
                int count = ((Number) countObj).intValue();
                if (day >= 1 && day <= 7) {
                    lastWeekCounts[day - 1] = count;
                }
            }
        }

        // 生成动态days数组（本周7天从thisWeekStart开始）
        String[] dynamicDays = new String[7];
        for (int i = 0; i < 7; i++) {
            LocalDate date = thisWeekStart.plusDays(i);
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();
            dynamicDays[i] = month + "/" + day;
        }

        // 动态周：本周只显示到今天为止，今天之后的使用上周数据
        int todayWeekIndex = todayDayOfWeek % 7; // 周一(1)->1, 周二(2)->2, ..., 周日(7)->0

        int[] dynamicThisWeek = new int[7];
        for (int i = 0; i < 7; i++) {
            if (i <= todayWeekIndex) {
                dynamicThisWeek[i] = thisWeekCounts[i];
            } else {
                dynamicThisWeek[i] = lastWeekCounts[i];
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("days", Arrays.asList(dynamicDays));
        result.put("thisWeek", Arrays.stream(dynamicThisWeek).boxed().toList());
        result.put("lastWeek", Arrays.stream(lastWeekCounts).boxed().toList());
        return result;
    }

    @Override
    public Map<String, Object> getVisitRank() {
        LocalDate today = LocalDate.now();
        LocalDateTime dayStart = today.atStartOfDay();
        LocalDateTime dayEnd = today.plusDays(1).atStartOfDay();

        List<Map<String, Object>> data = homeDashboardMapper.selectVisitRank(dayStart, dayEnd, 10);

        List<Map<String, Object>> list = new ArrayList<>();
        int rank = 1;
        for (Map<String, Object> row : data) {
            Map<String, Object> item = new HashMap<>();
            item.put("rank", rank++);
            item.put("doctor", row.get("doctor"));
            item.put("department", row.get("department"));
            item.put("count", row.get("count"));
            list.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        return result;
    }

    @Override
    public Map<String, Object> getFeeRank() {
        LocalDate today = LocalDate.now();
        LocalDateTime dayStart = today.atStartOfDay();
        LocalDateTime dayEnd = today.plusDays(1).atStartOfDay();

        List<Map<String, Object>> data = homeDashboardMapper.selectFeeRank(dayStart, dayEnd, 10);

        List<Map<String, Object>> list = new ArrayList<>();
        int rank = 1;
        for (Map<String, Object> row : data) {
            Map<String, Object> item = new HashMap<>();
            item.put("rank", rank++);
            item.put("doctor", row.get("doctor"));
            item.put("department", row.get("department"));
            item.put("fee", row.get("fee"));
            list.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        return result;
    }

    @Override
    public Map<String, Object> getRecentEvents() {
        List<Map<String, Object>> data = homeDashboardMapper.selectRecentEvents(LocalDateTime.now(), 20);

        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> row : data) {
            Map<String, Object> item = new HashMap<>();
            item.put("doctor", row.get("doctor"));
            item.put("patient", row.get("patient"));
            item.put("department", row.get("department"));

            Object orderTimeObj = row.get("orderTime");
            if (orderTimeObj instanceof LocalDateTime orderTime) {
                String dateStr = orderTime.toLocalDate().toString();
                int dayOfWeek = orderTime.getDayOfWeek().getValue();
                item.put("eventDate", dateStr + " " + DAY_NAMES[dayOfWeek % 7]);
            } else {
                item.put("eventDate", "");
            }
            list.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        return result;
    }

    @Override
    public Map<String, Object> getMySummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime dayStart = today.atStartOfDay();
        LocalDateTime dayEnd = today.plusDays(1).atStartOfDay();

        String doctor = getCurrentDoctor();

        Integer myReceived = homeDashboardMapper.selectMyReceivedCount(doctor, dayStart, dayEnd);
        Integer myPending = homeDashboardMapper.selectMyPendingCount(doctor, dayStart, dayEnd);
        BigDecimal myFee = homeDashboardMapper.selectMyTodayFee(doctor, dayStart, dayEnd);

        Map<String, Object> result = new HashMap<>();
        result.put("myReceivedCount", myReceived != null ? myReceived : 0);
        result.put("myPendingCount", myPending != null ? myPending : 0);
        result.put("myTodayFee", myFee != null ? myFee : BigDecimal.ZERO);
        return result;
    }

    private String getCurrentDoctor() {
        try {
            return BQRequestContextHolderUtils.getUserDetails().getNickname();
        } catch (Exception e) {
            return "";
        }
    }
}
