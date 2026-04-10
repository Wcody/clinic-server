/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQApiResult;
import com.qkplm.clinic.libcommon.utils.BQPathUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Wcke
 * @description: 因为有包范围，必须跟着项目走
 * @datetime: 2024-06-14 16:54
 */
@Slf4j
@ControllerAdvice
public class BQApiExceptionHandler {
    private static final String REG_DUPLICATE_ENTRY = "Duplicate entry '([^']*)' for key '([^.]*)\\.([^_]*)_";
    private static final Pattern pattern = Pattern.compile(REG_DUPLICATE_ENTRY);

    /**
     * 获取重复键值异常信息
     */
    private String getDuplicateKeyExceptionMessage(DuplicateKeyException duplicateKeyException) {
        String msg = Objects.requireNonNull(duplicateKeyException.getRootCause()).getMessage();
        String regex = "Duplicate entry '([^']*)' for key '([^.]*)\\.([^_]*)_";
        // 编译正则表达式
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            String entry = matcher.group(1);
            msg = String.format("%s已经存在", entry);
        }
        return msg;
    }

    /**
     * 获取异常信息
     */
    private String getExceptionMessage(Exception e) {
        String msg = e.getMessage();
        if (e instanceof BQApiException apiException) {
            msg = apiException.getMessage();
        } else if (e instanceof DuplicateKeyException duplicateKeyException) {
            msg = getDuplicateKeyExceptionMessage(duplicateKeyException);
        }
        return msg;
    }

    /**
     * 获取异常信息
     */
    private String getDetailedMessage(Exception e) {
        String message = e.getMessage();
        if (message == null || message.isEmpty()) {
            try {
                Field detailMessageField = Throwable.class.getDeclaredField("detailMessage");
                detailMessageField.setAccessible(true);
                message = (String) detailMessageField.get(e);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                // 如果获取detailMessage失败，使用一个默认的消息
                message = "No detailed message available";
            }
        }
        return message;
    }


    /**
     * 统一异常处理
     *
     * @param request  请求
     * @param response 响应
     * @param e        异常
     * @return 统一返回JSON
     * @throws IOException IO异常
     */
    @SneakyThrows
    @ExceptionHandler
    public Object handleException(HttpServletRequest request, HttpServletResponse response, final Exception e) throws IOException {
        //开发模式开启堆栈信息
        //e.printStackTrace();
        String uri = request.getRequestURI();
        log.error("执行出错：{}，{}，{}", uri, e.getClass().getName(), e.getMessage());
        log.error("堆栈信息如下：", e);
        //如果是需要授权的页面，默认授权页面都是需要返回JSON的，直接以JSON返回
        if (BQPathUtils.isApiPath(uri)) {
            String msg = getExceptionMessage(e);
            int code = (e instanceof BQApiException apiException) ? apiException.getCode() : -1;
            if (code == -1) {
                code = (e.getCause() instanceof BQApiException apiException) ? apiException.getCode() : 1;
            }
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(BQApiResult
                    .builder()
                    .code(code)
                    .message(msg)
                    .exception(e.getClass().getSimpleName())
                    .build()
                    .toJsonString());
            return null;
        }
        request.setAttribute("errorStatus", e instanceof NoResourceFoundException ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR);
        request.setAttribute("errorException", e.getClass().getSimpleName());
        request.setAttribute("errorUrl", uri);
        request.setAttribute("errorMessage", getDetailedMessage(e));
        // 使用 forward 而不是 redirect
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error");
        dispatcher.forward(request, response);
        return null;
    }
}
