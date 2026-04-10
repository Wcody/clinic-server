/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.security;

import com.qkplm.clinic.libcommon.utils.*;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.constant.BQRedisKeyConstant;
import com.qkplm.clinic.libcommon.utils.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Objects;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-14 23:26
 */
@Slf4j
@Component
public class BQAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    private HandlerExceptionResolver handlerExceptionResolver;
    @Value("${baqi.security.auth-prefix-arr}")
    private String[] authPrefixArr;

    /**
     * 判断是否需要授权
     */
    private boolean isAuthPrefix(String requestURI) {
        return BQPathUtils.matches(authPrefixArr, requestURI);
    }

    /**
     * 判断是否是白名单
     */
    private boolean isAuthWhitePrefix(String requestURI) {
        return BQSecurityPermissionUtils.isNonAuth(requestURI);
    }

    /**
     * 拦截器
     */
    @Override
    protected void doFilterInternal(
            @NonNull  HttpServletRequest request
            ,@NonNull HttpServletResponse response
            ,@NonNull FilterChain filterChain) {
        long startTime = System.currentTimeMillis();
        //设置用于计算耗时的开始时间
        BQRequestContextHolderUtils.setRequestBeginTime();
        try {
            String requestURI = request.getRequestURI();
            if (BQPathUtils.isAuthBlackPath(requestURI) || BQAuthUtils.checkAuth()) {
                if (isAuthPrefix(requestURI) && !isAuthWhitePrefix(requestURI)) {
                    // 只有授权路径并不是白名单的路径才需要鉴权
                    String requestToken = BQRequestContextHolderUtils.getRequestToken();
                    if (StringUtils.isNotBlank(requestToken)) {
                        // 判断token是否有效
                        boolean verifyToken = BQJwtUtils.isValidToken(requestToken);
                        if (!verifyToken) {
                            throw new BQApiException(1001, "token无效");
                        }
                        // 获取账户名
                        String account = BQJwtUtils.getSubject(requestToken);
                        // 获取平台信息
                        String platform = BQJwtUtils.getIssuer(requestToken);
                        // 是否符合要求
                        boolean found = StringUtils.isNotBlank(account)
                                && StringUtils.isNotBlank(platform);
                        log.info("账户:{},平台:{}", account, platform);
                        if (found) {
                            if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                                //拼接全键
                                String key = BQRedisKeyConstant.onlineUserKey(account, platform);
                                //从缓存获取数据
                                BQSecurityUserDetails userDetails = BQRedisUtils.get(key);
                                if (Objects.isNull(userDetails)) {
                                    throw new BQApiException(1001,"token已失效");
                                }
                                // 保存用户信息到当前会话
                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails,
                                                userDetails.getPassword(),
                                                userDetails.getAuthorities());
                                // 将authentication填充到安全上下文
                                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                // 初始化request参数
                                BQRequestContextHolderUtils.setUserDetails(userDetails);
                            }
                            //放行
                            filterChain.doFilter(request, response);
                        } else {
                            throw new BQApiException(1001, "非法的token");
                        }
                    } else {
                        throw new BQApiException(1001, "token无效");
                    }
                } else {
                    //放行
                    filterChain.doFilter(request, response);
                }
            }
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
            //不能直接跑出去，否则全局的ExceptionHandler无法捕获
            //throw new RuntimeException("token无效");
        } finally {
            log.info("请求地址：{}，请求耗时：{}ms", request.getRequestURI(), System.currentTimeMillis() - startTime);
        }
    }
}
