/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author: Wcke
 * @description: 日期工具类
 * @datetime: 2024-06-18 18:56
 */
public class BQDateUtils {
    public static String getCurrentDate() {
        return format("yyyy-MM-dd HH:mm:ss", new Date());
    }

    public static String getCurrentDateMills() {
        return format("yyyy-MM-dd HH:mm:ss", new Date());
    }

    public static String getCurrentDateMillsNoSep() {
        return format("yyyyMMddHHmmssSSS", new Date());
    }

    public static String getCurrentDateMillsNoSepShortYear() {
        return format("yyMMddHHmmssSSS", new Date());
    }

    public static String getTodayStr() {
        return format("yyyy-MM-dd", LocalDateTime.now());
    }

    public static String getNextDateStr() {
        return format("yyyy-MM-dd", LocalDateTime.now().plusDays(1));
    }

    public static String format(String fmt, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(fmt, Locale.CHINA);
        return dateFormat.format(date);
    }

    public static String format(String fmt, LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt, Locale.CHINA);
        return date.format(formatter);
    }

    public static long milliSeconds(LocalDateTime begin, LocalDateTime end) {
        Instant beginInstant = begin.toInstant(ZoneOffset.of("+8"));
        Instant endInstant = end.toInstant(ZoneOffset.of("+8"));
        return endInstant.toEpochMilli() - beginInstant.toEpochMilli();
    }

    /**
     * 格式化年龄字符串
     */
    public static String formatAge(Integer firstAge, Integer lastAge, Integer ageType) {
        if (firstAge == null)
            firstAge = 0;
        if (lastAge == null)
            lastAge = 0;

        if (ageType == 1) {
            // 年龄类型为年
            if (firstAge > 0 && lastAge > 0) {
                return firstAge + "岁" + lastAge + "月";
            } else if (firstAge > 0) {
                return firstAge + "岁";
            } else if (lastAge > 0) {
                return lastAge + "月";
            } else {
                return "0岁";
            }
        } else if (ageType == 2) {
            // 年龄类型为月
            if (firstAge > 0 && lastAge > 0) {
                return firstAge + "月" + lastAge + "天";
            } else if (firstAge > 0) {
                return firstAge + "月";
            } else if (lastAge > 0) {
                return lastAge + "天";
            } else {
                return "0月";
            }
        }
        return "未知";
    }
}
