/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-15 17:42
 */
@Slf4j
public class BQIDUtils {
    /**
     * 盐值
     */
    private static final String SLAT = "&b%a8q7i0.0t&e&c0h%0%$8$#7@";

    /**
     * 获取UUID，没有-，小写字符串
     */
    static public String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    /**
     * 获取M字符串的MD5，默认加盐值
     */
    static public String md5(String str) {
        return md5(str, SLAT);
    }

    static public String md5(String str, String slat) {
        try {
            str += slat;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = m.digest();
            StringBuilder result = new StringBuilder();
            for (byte aByte : bytes) {
                result.append(Integer.toHexString((0x000000FF & aByte) | 0xFFFFFF00).substring(6));
            }
            return result.toString();
        } catch (Exception e) {
            log.error("md5 error", e);
        }
        return null;
    }

//    public static void main(String[] args) {
//        System.out.println(uuid());
//        System.out.println(uuid());
//        System.out.println(uuid());
//        System.out.println(uuid());
//        System.out.println(uuid());
//        System.out.println(uuid());
//        System.out.println(uuid());
//        System.out.println(uuid());
//        System.out.println(uuid());
//        System.out.println(uuid());
//    }
}
