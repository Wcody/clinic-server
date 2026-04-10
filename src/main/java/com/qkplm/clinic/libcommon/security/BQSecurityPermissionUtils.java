/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.security;

import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.utils.BQPathUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;

/**
 * @author: Wcke
 * @description: 安全鉴权类，系统所有接口都需要经过该类鉴权
 * @datetime: 2024-06-16 18:04
 */
@Slf4j
@Getter
public class BQSecurityPermissionUtils {
    private static WebApplicationContext applicationContext;
    /**
     * 私有构造方法
     */
    private BQSecurityPermissionUtils() {
    }

    /**
     * 系统所有需要认证和授权的资源列表，needAuth=true, needGrant=true
     */
    @Getter
    private static final List<BQSecurityPermission> allPermissions = new ArrayList<>();

    /**
     * 不需要认证访问的资源URI列表，needAuth=false
     */
    @Getter
    private static final List<String> nonAuthPermissions = new ArrayList<>();

    /**
     * 不需要授权访问的资源URI列表, needAuth=true, needGrant=false
     */
    @Getter
    private static final List<String> nonGrantPermissions = new ArrayList<>();

    /**
     * 路径与日志注解映射
     * 用于异常时根据路径获取日志注解
     */
    @Getter
    private static final Map<String, BQLogMark> logMarkMap = new HashMap<>();

    /**
     * 路径与权限注解映射
     * 用于数据权限的运算
     */
    @Getter
    private static final Map<String, BQAuthMark> authMarkMap = new HashMap<>();

    /**
     * 按钮和权限的映射表
     * 例如：BQMenuButtons:save,menu:save
     */
    @Getter
    private static final Map<String, Set<String>> buttonAuthMap = new HashMap<>();

    /**
     * 鉴权
     */
    public static boolean check(String requestUri, Collection<? extends GrantedAuthority> authorities) {
        long startTime = System.currentTimeMillis();
        try {
            //todo: 暂时全部方向，后续修补
            return !requestUri.isEmpty();
//            List<String> uriPermissions = getUriPermissions(requestUri);
//            if (!uriPermissions.isEmpty()) {
//                for (GrantedAuthority authority : authorities) {
//                    if (uriPermissions.contains(authority.getAuthority())) {
//                        return true;
//                    }
//                }
//            }
        } finally {
            log.info("检查权限耗时: {}ms", System.currentTimeMillis() - startTime);
        }
        //return false;
    }

    /**
     * 获取当前连接的所有可访问权限
     */
    private static List<String> getUriPermissions(String requestUri) {
        List<String> uriPermissions = new ArrayList<>();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (BQSecurityPermission permission : allPermissions) {
            if (antPathMatcher.match(permission.getPattern(), requestUri)) {
                uriPermissions.add(permission.getAuth());
            }
        }
        return uriPermissions;
    }

    /**
     * 启动启动时，初始化系统所有权限
     */
    public static void initAllPermissions(WebApplicationContext applicationContext) {
        if (!allPermissions.isEmpty()) {
            return;
        }
        long startTime = System.currentTimeMillis();
        try {
            BQSecurityPermissionUtils.applicationContext = applicationContext;
            RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
            Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
                RequestMappingInfo info = entry.getKey();
                HandlerMethod method = entry.getValue();
                PathPatternsRequestCondition patternsCondition = info.getPathPatternsCondition();
                BQAuthMark annotation = method.getMethod().getAnnotation(BQAuthMark.class);
                if (Objects.nonNull(annotation) && Objects.nonNull(patternsCondition)) {
                    // 接口访问的权限值
                    String auth = String.format("%s:%s", annotation.tag(), method.getMethod().getName());
                    // 获取按钮列表
                    List<String> buttons = Arrays.asList(annotation.buttons());
                    // 遍历按钮列表
                    for (String button : buttons) {
                        Set<String> auths = buttonAuthMap.get(button);
                        if (Objects.isNull(auths)) {
                            auths = new HashSet<>();
                            buttonAuthMap.put(button, auths);
                        }
                        // 加入按钮可访问的权限值，最终会写入菜单表的按钮项目
                        auths.add(auth);
                    }
                    //获取日志注解
                    BQLogMark logMark = method.getMethod().getAnnotation(BQLogMark.class);
                    // 获取路径
                    for (PathPattern url : patternsCondition.getPatterns()) {
                        String uri = url.getPatternString();
                        //记录路径注解映射
                        if (Objects.nonNull(logMark)) {
                            logMarkMap.put(uri, logMark);
                        }
                        //记录权限注解映射
                        if (Objects.nonNull(logMark)) {
                            authMarkMap.put(uri, annotation);
                        }
                        String methodName = method.getMethod().getName();
                        BQSecurityPermission permission = BQSecurityPermission.builder()
                                .auth(auth)
                                .version(annotation.version())
                                .path(uri)
                                .clazz(method.getMethod().getDeclaringClass().getName())
                                .method(methodName)
                                .buttons(buttons)
                                .build();
                        //记录权限信息
                        if (annotation.needAuth() && annotation.needGrant()) {
                            // 需要认证也需要授权
                            allPermissions.add(permission);
                        } else if (annotation.needAuth()) {
                            // 需要认证不需要授权
                            nonGrantPermissions.add(uri);
                        } else {
                            // 不需要认证不需要授权
                            nonAuthPermissions.add(uri);
                        }
                    }
                }
            }
            //系统设计者放行所有资源
            allPermissions.add(BQSecurityPermission.builder()
                    .auth("super")
                    .version("v1")
                    .path("/**")
                    .build()
            );
            //平台管理员放行所有资源
            allPermissions.add(BQSecurityPermission.builder()
                    .auth("admin")
                    .version("v1")
                    .path("/**")
                    .build()
            );
        } finally {
            log.info("初始化系统所有权限耗时: {}ms", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * 根据URI获取可能存在的BQLogMark注解
     */
    public static BQLogMark getLogMark(String requestUri) {
        //不能直接获取，因为可能存在多个匹配
        //return logMarkMap.get(requestUri);
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        //获取匹配的注解
        return logMarkMap.entrySet().stream()
                .filter(entry -> antPathMatcher.match(entry.getKey(), requestUri))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据URI获取可能存在的BQLogMark注解
     */
    public static BQAuthMark getAuthMark(String requestUri) {
        //不能直接获取，因为可能存在多个匹配
        //return logMarkMap.get(requestUri);
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        //获取匹配的注解
        return authMarkMap.entrySet().stream()
                .filter(entry -> antPathMatcher.match(entry.getKey(), requestUri))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据按钮标识获取权限值
     */
    public static String getButtonAuths(String button) {
        Set<String> strings = buttonAuthMap.computeIfAbsent(button, k -> new HashSet<>());
        return String.join(",", strings);
    }

    /**
     * 是否是不需要认证的资源
     */
    public static boolean isNonAuth(String requestUri) {
        return BQPathUtils.matches(nonAuthPermissions, requestUri);
    }

    /**
     * 是否是不需要授权的资源
     */
    public static boolean isNonGrant(String requestUri) {
        return BQPathUtils.matches(nonGrantPermissions, requestUri);
    }
}