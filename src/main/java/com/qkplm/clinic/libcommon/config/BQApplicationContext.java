/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-16 12:31
 */
@Component
public class BQApplicationContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static String getRedisUsersPrefix() {
        String property = applicationContext.getEnvironment().getProperty("baqi.redis.prefix.users");
        if (StringUtils.isBlank(property)) {
            property = "unknown";
        }
        return property;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        BQApplicationContext.applicationContext = applicationContext;
    }
}
