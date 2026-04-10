/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author: Wcke
 * @description: IO工具类
 * @datetime: 2024-07-07 11:01
 */
@Slf4j
public class BQIOUtils {

    /**
     * 获取类路径下的资源路径，URL
     */
    public static URL getClassPathURL(String targetPath) {
        return BQIOUtils.class.getClassLoader().getResource(targetPath);
    }

    /**
     * 获取类路径下的资源路径，物理路径
     */
    public static String getClassPathPath(String targetPath) throws URISyntaxException {
        URL url = getClassPathURL(targetPath);
        if (Objects.nonNull(url)) {
            return Paths.get(url.toURI()).toString();
        }
        return null;
    }

    /**
     * 获取JAR根目录下的静态资源路径，用于外置资源的引用，优先使用外置资源
     */
    public static String getAppPathIfExists(String targetPath, String defaultPath) {
        URL url = getClassPathURL(targetPath);
        if (Objects.nonNull(url)) {
            log.info("url:{}", url);
            log.info("protocol:{}", url.getProtocol());
            //如果是jar运行，则资源外置
            if (url.getProtocol().equals("jar")) {
                //获取jar所在路径
                ApplicationHome applicationHome = new ApplicationHome(BQIOUtils.class);
                File parentFile = applicationHome.getSource().getParentFile();
                String jarPath = parentFile.getAbsolutePath();
                log.info("jarPath:{}", jarPath);
                File staticFile = new File(parentFile, targetPath);
                if (staticFile.exists()) {
                    return staticFile.getAbsolutePath();
                }
            }
        }
        return defaultPath;
    }

    /**
     * 获取JAR根目录下的静态资源路径，用于外置资源的引用，优先使用外置资源
     */
    public static String getAppPathIfExists(String targetPath) {
        return getAppPathIfExists(targetPath, null);
    }

    /**
     * 是否是开发模式
     */
    public static boolean isDevMode() {
        URL url = getClassPathURL("");
        return !url.getProtocol().equals("jar");
    }
}
