/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;

/**
 * @author: Wcke
 * @description: 处理上传文件时的工具类
 * @datetime: 2024-07-15 15:41
 */
public class BQUploadHandlerUtils {
    // 上传文件根路径
    public static final String UPLOAD_ROOT_PATH = "/uploads";
    // 上传分类模板路径
    public static final String TEMPLATE_PATH = "/template";
    // 病案文件上传路径
    public static final String MEDICAL_PATH = "/medical";
    // 档案文件上传路径
    public static final String FILES_PATH = "/files";
    // 图片上传路径
    public static final String IMAGES_PATH = "/images";
    // PDF上传路径
    public static final String PDFS_PATH = "/pdfs";

    // 获取根路径的完整路径
    public static String getRootPath() {
        String defaultPath = System.getProperty("user.dir") + UPLOAD_ROOT_PATH;
        return BQIOUtils.getAppPathIfExists(UPLOAD_ROOT_PATH, defaultPath);
    }

    // 获取当前文件子路径/year/month/day/filename
    public static String getSubPath(String filename) {
        return String.format("%s/%s", BQDateUtils.format("/yyyy/MM/dd", new Date()), filename);
    }

//    public static void main(String[] args) {
//        System.out.println(getRootPath());
//        System.out.println(getSubPath("123.txt"));
//        System.out.println(getTemplatePath("123.txt"));
//        System.out.println(getMedicalPath("123.txt"));
//        System.out.println(getFilesPath("123.txt"));
//    }

    public static String getTemplatePath(String filename) {
        return getTemplatePathBySubPath(getSubPath(filename));
    }

    public static File getTemplateFile(String filename) {
        return getTemplateFileBySubPath(getSubPath(filename));
    }

    public static String getTemplatePathBySubPath(String subPath) {
        return getTemplateFileBySubPath(subPath).getAbsolutePath();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File getTemplateFileBySubPath(String subPath) {
        File file = Paths.get(getRootPath(), TEMPLATE_PATH, subPath).toFile();
        file.getParentFile().mkdirs();
        return file;
    }

    public static File getMedicalFileBySubPath(String subPath) {
        File file = Paths.get(getRootPath(), MEDICAL_PATH, subPath).toFile();
        file.getParentFile().mkdirs();
        return file;
    }

    public static File getUploadImageBySubPath(String subPath) {
        File file = Paths.get(getRootPath(), IMAGES_PATH, subPath).toFile();
        file.getParentFile().mkdirs();
        return file;
    }

    public static String getMedicalPath(String filename) {
        return Paths.get(getRootPath(), MEDICAL_PATH, getSubPath(filename)).toFile().getAbsolutePath();
    }

    public static String getFilesPath(String filename) {
        return Paths.get(getRootPath(), FILES_PATH, getSubPath(filename)).toFile().getAbsolutePath();
    }

    // 获取PDF上传根路径
    public static String getPdfsPath(String subPath) {
        return Paths.get(getRootPath(), PDFS_PATH, subPath).toString();
    }

    // 获取PDF上传根路径
    public static String getPdfsPath() {
        return getPdfsPath("");
    }

    // 获取PDF上传根路径对象
    public static File getPdfsPathBySubPath(String subPath) {
        File file = Paths.get(getRootPath(), PDFS_PATH, subPath).toFile();
        file.getParentFile().mkdirs();
        return file;
    }
}
