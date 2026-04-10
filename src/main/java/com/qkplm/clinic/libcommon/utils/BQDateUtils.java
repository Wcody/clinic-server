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

    public static String format(String fmt, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(fmt, Locale.CHINA);
        return dateFormat.format(date);
    }

    public static long milliSeconds(LocalDateTime begin, LocalDateTime end) {
        Instant beginInstant = begin.toInstant(ZoneOffset.of("+8"));
        Instant endInstant = end.toInstant(ZoneOffset.of("+8"));
        return endInstant.toEpochMilli() - beginInstant.toEpochMilli();
    }
}
