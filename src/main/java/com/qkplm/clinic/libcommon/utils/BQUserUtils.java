/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-15 20:22
 */
public class BQUserUtils {
    static public final String SALT_PASSWORD = "www.baqi.tech";
    static public String getRealPassword(String rawPassword) {
        return BQIDUtils.md5(rawPassword, SALT_PASSWORD);
    }
}
