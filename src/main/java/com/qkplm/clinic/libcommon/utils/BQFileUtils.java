/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-15 17:42
 */
@Slf4j
public class BQFileUtils {
    /**
     * 盐值
     */
    private static final String SLAT = "&b%a8q7i0.0t&e&c0h%0%$8$#7@";

    /**
     * 计算文件内容的md5值
     */
    public static String getFileMD5(String filePath) throws Exception {
        return getFileMD5(new File(filePath));
    }

    /**
     * 计算文件内容的md5值
     */
    public static String getFileMD5(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (FileInputStream fis = new FileInputStream(file);
             FileChannel channel = fis.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            while (channel.read(buffer) != -1) {
                buffer.flip();
                md.update(buffer);
                buffer.clear();
            }
        }
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 删除目录下符合正则匹配的所有文件
     */
    public static void deleteFilesBy(String path, String regex) {
        File dir = new File(path);
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.getName().matches(regex)) {
                file.delete();
            }
        }
    }
}
