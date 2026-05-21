/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qkplm.clinic.libcommon.security.BQSecurityUserDetails;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.qkplm.clinic.libcommon.enums.BQPlatformEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-12 18:57
 */
@Slf4j
public class BQRequestContextHolderUtils {
    static private final String BEGIN_TIME_NAME = "__BEGIN_TIME__";
    static private final String STATIC_USERID = "1915e15d0a5c41eea25f0c546499faa2";
    static private final String STATIC_ACCOUNT = "admin";
    static private final String STATIC_TENANT_ID = "04bda4f00fc44642f41bfafbb5c6f280";
    static public final String PLATFORM_TENANT_ID = STATIC_TENANT_ID;
    static private final BQPlatformEnum STATIC_PLATFORM = BQPlatformEnum.PCB;
    static private final String REAL_IP_NAME = "X-Real-IP";
    static private final String HOST_NAME = "Host";
    static private final String USER_DETAILS_NAME = "USER_DETAILS_NAME";
    static private final String SOURCE_TENANT_NAME = "SOURCE_TENANT_NAME";
    static private final String LOGIN_TENANT_NAME = "LOGIN_TENANT_NAME";

    /**
     * 检验用户信息
     */
    static private BQSecurityUserDetails checkUserDetails(BQSecurityUserDetails userDetails) {
        if (StringUtils.isBlank(userDetails.getAccount())) {
            userDetails.setAccount(STATIC_ACCOUNT);
        }
        if (StringUtils.isBlank(userDetails.getEid())) {
            userDetails.setEid(STATIC_USERID);
        }
//        if (StringUtils.isBlank(userDetails.getTenantId())) {
//            userDetails.setTenantId(STATIC_TENANT_ID);
//        }
        if (Objects.isNull(userDetails.getPlatform())) {
            userDetails.setPlatform(STATIC_PLATFORM);
        }
        return userDetails;
    }
    /**
     * 获取请求属性
     */
    static public ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
    /**
     * 获取当前请求对象
     */
    static public HttpServletRequest getRequest() {
        return (HttpServletRequest) getRequestAttributes().resolveReference(RequestAttributes.REFERENCE_REQUEST);
    }

    /**
     * 获取原始租户ID，更新租户管理员信息的时候需要用到
     */
    static public String getSourceTenantId() {
        String tenantId = null;
        HttpServletRequest request = getRequest();
        if (!Objects.isNull(request)) {
            tenantId = (String) request.getAttribute(SOURCE_TENANT_NAME);
            if (StringUtils.isBlank(tenantId)) {
                tenantId = (String) request.getAttribute(LOGIN_TENANT_NAME);;
            }
        }
        return tenantId;
    }

    /**
     * 获取原始租户ID
     */
    static public void setSourceTenantId() {
        HttpServletRequest request = getRequest();
        if (!Objects.isNull(request)) {
            String sourceTenantId = getUserDetails().getSourceTenantId();
            request.setAttribute(SOURCE_TENANT_NAME, sourceTenantId);
        }
    }

    /**
     * 设置原始租户ID
     */
    static public void setSourceTenantId(String tenantId) {
        HttpServletRequest request = getRequest();
        if (!Objects.isNull(request)) {
            request.setAttribute(SOURCE_TENANT_NAME, tenantId);
        }
    }

    /**
     * 清除原始租户ID
     */
    static public void removeSourceTenantId() {
        HttpServletRequest request = getRequest();
        if (!Objects.isNull(request)) {
            request.removeAttribute(SOURCE_TENANT_NAME);
        }
    }

    /**
     * 获取当前响应对象
     */
    static public HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取当前登录用户明细
     */
    static public BQSecurityUserDetails getUserDetails() {
        HttpServletRequest request = getRequest();
        Object userDetails = request.getAttribute(USER_DETAILS_NAME);
        if (Objects.isNull(userDetails)) {
            log.error("未获取到当前登录用户信息，将采用缺省对象返回");
            userDetails = BQSecurityUserDetails.builder().build();
        }
        return checkUserDetails((BQSecurityUserDetails) userDetails);
    }

    /**
     * 设置当前用户明细信息
     */
    static public void setUserDetails(BQSecurityUserDetails userDetails) {
        HttpServletRequest request = getRequest();
        request.setAttribute(USER_DETAILS_NAME, userDetails);
    }

    /**
     * 获取当前请求IP
     */
    static public String getRealRemoteIP() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader(REAL_IP_NAME);
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取当前请求IP
     */
    static public int getRealRemotePort() {
        HttpServletRequest request = getRequest();
        return request.getRemotePort();
    }

    /**
     * 获取当前请求HOST
     */
    static public String getRealHost() {
        HttpServletRequest request = getRequest();
        String host = request.getHeader(HOST_NAME);
        if (StringUtils.isBlank(host)) {
            host = request.getServerName() + ":" + request.getServerPort();
        }
        if (host.endsWith(":80")) {
            host = host.replace(":80", "");
        }
        return host;
    }

    static public ObjectNode getRequestParams() {
        ObjectNode node = BQJacksonUtils.newObjectNode();
        node.put("params", getRequest().getQueryString());
        return node;
    }

    static public ObjectNode getRequestHeaders() {
        ObjectNode node = BQJacksonUtils.newObjectNode();
        getRequest().getHeaderNames().asIterator().forEachRemaining(name -> {
            node.put(name, getRequest().getHeader(name));
        });
        return node;
    }

    static public ObjectNode getRequestBody() {
        ObjectNode node = BQJacksonUtils.newObjectNode();
        node.put("body", getRequest().getQueryString());
        return node;
    }

    static public ObjectNode getResponseHeaders() {
        ObjectNode node = BQJacksonUtils.newObjectNode();
        getResponse().getHeaderNames().forEach(name -> {
            node.put(name, getResponse().getHeader(name));
        });
        return node;
    }

    /**
     * 设置请求开始时间，用于请求耗时计算
     */
    static public LocalDateTime setRequestBeginTime() {
        LocalDateTime beginTime = LocalDateTime.now();
        getRequest().setAttribute(BEGIN_TIME_NAME, beginTime);
        return beginTime;
    }

    /**
     * 获取请求开始时间，用于请求耗时计算
     */
    static public LocalDateTime getRequestBeginTime() {
        return (LocalDateTime) getRequest().getAttribute(BEGIN_TIME_NAME);
    }

    /**
     * 获取请求的Token
     */
    static public String getRequestToken() throws JsonProcessingException {
        // 从Header中获取
        String token = getRequest().getHeader(BQJwtUtils.getCurrentConfig().getHeader());
        if (StringUtils.isBlank(token)) {
            //从Cookie中获取
            Cookie[] cookies = getRequest().getCookies();
            if (!Objects.isNull(cookies)) {
                for (Cookie cookie : cookies) {
                    if ("authorized-token".equalsIgnoreCase(cookie.getName())) {
                        token = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                        Map<String, String> tokenMap = BQJacksonUtils.jsonToBean(token, new TypeReference<Map<String, String>>() {});
                        token = "Bearer " + tokenMap.get("accessToken");
                        break;
                    }
                }
            }
        }
        return token;
    }

    /**
     * 设置登录租户ID
     */
    static public void setLoginTenantId(String tenantId) {
        HttpServletRequest request = getRequest();
        request.setAttribute(LOGIN_TENANT_NAME, tenantId);
    }

    /**
     * 获取登录租户ID
     */
    static public String getLoginTenantId() {
        HttpServletRequest request = getRequest();
        return (String) request.getAttribute(LOGIN_TENANT_NAME);
    }

    /**
     * 移除登录租户ID
     */
    static public void removeLoginTenantId() {
        HttpServletRequest request = getRequest();
        request.removeAttribute(LOGIN_TENANT_NAME);
    }
}
