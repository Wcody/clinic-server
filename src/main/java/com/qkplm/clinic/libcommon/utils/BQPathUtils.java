/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-15 19:42
 */
public class BQPathUtils {
    static public boolean match(String pattern, String path) {
        return new AntPathMatcher().match(pattern, path);
    }

    static public boolean matches(String[] patterns, String path) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String pattern : patterns) {
            if (antPathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    static public boolean matches(List<String> patterns, String path) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String pattern : patterns) {
            if (antPathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    static public boolean isApiPath(String path) {
        return match("/*/api/**", path);
    }

    static public boolean isAuthBlackPath(String path) {
        return match("/*/api/*/user/sysInfo", path) || !isApiPath(path);
    }
}
