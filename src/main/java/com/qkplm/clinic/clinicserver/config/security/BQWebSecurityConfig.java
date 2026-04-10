/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.config.security;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import com.qkplm.clinic.clinicserver.service.impl.security.BQSecurityUserDetailsService;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.security.BQAuthenticationFilter;
import com.qkplm.clinic.libcommon.security.BQSecurityPermissionUtils;

import java.util.Collection;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-14 16:02
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class BQWebSecurityConfig {
    @Value("${baqi.security.auth-prefix-arr}")
    private String[] authPrefixArr;
    @Resource
    private BQSecurityUserDetailsService userDetailsService;
    @Resource
    private BQAuthenticationFilter authenticationFilter;
    @Resource
    private HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 禁用明文验证
                .httpBasic(AbstractHttpConfigurer::disable)
                // 关闭csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用默认登录页
                .formLogin(AbstractHttpConfigurer::disable)
                // 禁用默认登出页
                .logout(AbstractHttpConfigurer::disable)
                // 禁用session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置拦截信息
                .authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
                                .requestMatchers(authPrefixArr)
                                .access((authentication, object) -> {
                                    // 已经验证通过，在此进行操作权限校验
                                    // 获取当前的访问路径
                                    String requestURI = object.getRequest().getRequestURI();
                                    // 不需要认证的白名单和不需要鉴权的白名单都直接放行
                                    if (
                                            BQSecurityPermissionUtils.isNonAuth(requestURI) ||
                                                    BQSecurityPermissionUtils.isNonGrant(requestURI)
                                    ) {
                                        return new AuthorizationDecision(true);
                                    }
                                    // 获取当前登录用户权限信息
                                    Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
                                    // 返回鉴权结果
                                    return new AuthorizationDecision(BQSecurityPermissionUtils.check(requestURI, authorities));
                                })
                                .anyRequest().permitAll()
                )
                .exceptionHandling(exceptionHandlingCustomizer -> exceptionHandlingCustomizer
                        //认证后，接口没有权限时触发，403
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            handlerExceptionResolver.resolveException(request, response, null, new BQApiException(403, "无访问权限"));
                        })
                        //未认证，访问接口时触发，401
                        .authenticationEntryPoint((request, response, authException) -> {
                            handlerExceptionResolver.resolveException(request, response, null, new BQApiException(401, "请登录后再进行访问"));
                        })
                )
                .userDetailsService(userDetailsService)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
