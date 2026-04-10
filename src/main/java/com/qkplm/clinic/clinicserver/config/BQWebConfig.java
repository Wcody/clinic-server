/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.config;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.qkplm.clinic.libcommon.utils.BQIOUtils;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-17 09:24
 */
@Slf4j
@Configuration
public class BQWebConfig implements WebMvcConfigurer {
    /**
     * 跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");

    }

    /**
     * 静态资源
     */
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String staticPath = "classpath:/static/";
        String appStaticPath = BQIOUtils.getAppPathIfExists("static", staticPath);
        if (!appStaticPath.equals(staticPath)) {
            staticPath = "file:" + appStaticPath + "/";
        }
        log.info("staticPath:{}", staticPath);
        registry.addResourceHandler("/**")
                .addResourceLocations(staticPath);
    }
}
