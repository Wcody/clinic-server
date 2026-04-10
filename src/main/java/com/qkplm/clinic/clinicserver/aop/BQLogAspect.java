/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import com.qkplm.clinic.clinicserver.entity.BqLogEntity;
import com.qkplm.clinic.clinicserver.service.IBqLogService;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.enums.BQLogTypeEnum;
import com.qkplm.clinic.libcommon.security.BQSecurityPermissionUtils;
import com.qkplm.clinic.libcommon.security.BQSecurityUserDetails;
import com.qkplm.clinic.libcommon.utils.BQDateUtils;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-26 17:25
 */
@Slf4j
@Component
@Aspect
public class BQLogAspect {
    private final IBqLogService LogService;

    public BQLogAspect(IBqLogService logService) {
        LogService = logService;
    }

    private String traceToString(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }

    private void saveLog(BQLogMark logMark, Object result, Exception exception) {
        LocalDateTime beginTime = BQRequestContextHolderUtils.getRequestBeginTime();
        LocalDateTime endTime = LocalDateTime.now();
        Long costTime = BQDateUtils.milliSeconds(beginTime, endTime);
        BQSecurityUserDetails userDetails = BQRequestContextHolderUtils.getUserDetails();
        BqLogEntity logEntity = new BqLogEntity();
        logEntity.setBeginTime(beginTime);
        logEntity.setEndTime(endTime);
        logEntity.setCostTime(costTime);
        if (Objects.isNull(exception) || exception instanceof BQApiException) {
            // 登录日志、注销日志、操作日志
            logEntity.setLogType(logMark.loginType().getCode());
        } else {
            //系统日志
            logEntity.setLogType(BQLogTypeEnum.SYSTEM.getCode());
        }
        logEntity.setModule(logMark.module());
        logEntity.setOperation(logMark.operation());
        logEntity.setRequestIp(BQRequestContextHolderUtils.getRealRemoteIP());
        logEntity.setRequestUri(BQRequestContextHolderUtils.getRequest().getRequestURI());
        logEntity.setRequestAddress(BQRequestContextHolderUtils.getRealHost());
        logEntity.setMethod(BQRequestContextHolderUtils.getRequest().getMethod());
        logEntity.setRequestParams(BQRequestContextHolderUtils.getRequestParams());
        logEntity.setRequestHeader(BQRequestContextHolderUtils.getRequestHeaders());
        logEntity.setRequestBody(BQRequestContextHolderUtils.getRequestBody());
        logEntity.setResponseHeader(BQRequestContextHolderUtils.getResponseHeaders());
        logEntity.setAccount(userDetails.getAccount());
        logEntity.setTenantId(userDetails.getTenantId());
        logEntity.setPlatform(userDetails.getPlatform().getName());
        logEntity.setUserAgent(BQRequestContextHolderUtils.getRequest().getHeader("User-Agent"));
        logEntity.setOs("Windows 11");
        logEntity.setBrowser("Chrome");
        if (exception == null) {
            logEntity.setStatus(true);
            logEntity.setResponseBody(BQJacksonUtils.newObjectNode().putPOJO("body", result));
        } else {
            logEntity.setStatus(false);
            logEntity.setExceptionInfo(traceToString(exception));
        }

        // 写入日志
        //log.info(BQJacksonUtils.toJson(logEntity));
        //LogService.save(logEntity);
    }

//    @Before("@annotation(logMark)")
//    public void before(BQLogMark logMark) {
//        //提前到BQAuthenticationFilter入口获取时间
//        //BQRequestContextHolderUtils.getRequest().setAttribute("_beginTime", LocalDateTime.now());
//    }

    @AfterReturning(value = "@annotation(logMark)", returning = "result")
    public void afterReturning(BQLogMark logMark, Object result) {
        saveLog(logMark, result, null);
    }

//    @AfterThrowing(value = "@annotation(logMark)", throwing = "exception")
//    public void afterThrowing(BQLogMark logMark, Exception exception) {
//        //.info("----------------异常处理切面: {}", "afterThrowing");
//        //saveLog(logMark, null, exception);
//        //统一由BQApiExceptionHandler处理
//    }

    @Around("execution(* com.qkplm.clinic.libcommon.config.BQApiExceptionHandler.handleException(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = (HttpServletRequest) args[0];
        Exception exception = (Exception) args[2];
        String uri = request.getRequestURI();
        BQLogMark logMark = BQSecurityPermissionUtils.getLogMark(uri);
        Object result = joinPoint.proceed();
        if (Objects.nonNull(logMark)) {
            saveLog(logMark, null, exception);
        }
        return result;
    }
}
