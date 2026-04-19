/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.qkplm.clinic.libcommon.utils.BQIOUtils;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;

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

    /**
     * 配置Jackson时间序列化/反序列化器
     * 支持ISO 8601格式（如：2026-04-13T06:41:43.454Z）
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 使用BQJacksonUtils中已配置好的ObjectMapper，包含忽略未知属性等全局配置
        ObjectMapper objectMapper = BQJacksonUtils.getMapper();
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
