/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.config;

import com.qkplm.clinic.libcommon.security.BQBaqiPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-17 09:19
 */
@Slf4j
@Configuration
public class BQSecurityBaseConfig {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        //可以升级的密码加密策略
        //当前用的密码策略
        String encodingId = "baqi11";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("baqi11", new BCryptPasswordEncoder());
        encoders.put("baqi22", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("baqi33", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("baqi66", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("baqi87", new BQBaqiPasswordEncoder());
        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);
        //设置缺省值
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(encoders.get(encodingId));
        return delegatingPasswordEncoder;
        //{bcrypt}只固定一种，无法升级，不推荐
        //return new BCryptPasswordEncoder();
    }
}
