/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import com.qkplm.clinic.libcommon.api.BQApiException;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author: Wcke
 * @description: OCR常用工具类
 * @datetime: 2025-01-01 13:07
 */
@Slf4j
public class BQOcrUtils {
    private final static String OCR_URL = "http://127.0.0.1:8000/ocr2txt";
    private final static String OCR_URL_ONLY = "http://127.0.0.1:8000/ocr2txt/only";
    private final static String DEV_OCR_URL = "http://192.168.44.200:8000/ocr2txt";
    private final static String DEV_OCR_URL_ONLY = "http://192.168.44.200:8000/ocr2txt/only";
    private final static String DEV_OCR_URL_ONLY_KEEP = "http://192.168.44.200:8000/ocr2txt/keep";
    private final static String OCR_URL_ONLY_KEEP = "http://192.168.44.200:8000/ocr2txt/keep";

    private static String getOcrUrl() {
        return BQIOUtils.isDevMode() ? DEV_OCR_URL : OCR_URL;
    }

    private static String getOcrUrlOnly() {
        return BQIOUtils.isDevMode() ? DEV_OCR_URL_ONLY : OCR_URL_ONLY;
    }

    private static String getOcrUrlKeep() {
        return BQIOUtils.isDevMode() ? DEV_OCR_URL_ONLY_KEEP : OCR_URL_ONLY_KEEP;
    }

    public static String ocr2txt(String imagePath) {
        return ocr2txt(new File(imagePath));
    }

    public static String ocr2txt(File imageFile) {
        // 创建HTTP客户端
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建POST请求
            HttpPost httpPost = new HttpPost(getOcrUrl());

            // 使用 MultipartEntityBuilder 构建多部分表单
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addBinaryBody("file", imageFile, ContentType.DEFAULT_BINARY, imageFile.getName())
                    .build();

            // 设置请求实体
            return getStringBody(httpClient, httpPost, entity);
        } catch (IOException e) {
            log.error("ocr2txt occurred while making HTTP request: {}", e.getMessage());
            throw new BQApiException("Error occurred while making HTTP request");
        }
    }

    public static String ocr2txtOnly(String imagePath, int left, int top, int width, int height) {
        return ocr2txtOnly(new File(imagePath), left, top, width, height);
    }

    public static String ocr2txtKeep(String imagePath, int left, int top, int width, int height) {
        return getTxtFromUrl(getOcrUrlKeep(), new File(imagePath), left, top, width, height);
    }

    public static String ocr2txtOnly(File imageFile, int left, int top, int width, int height) {
        return getTxtFromUrl(getOcrUrlOnly(), imageFile, left, top, width, height);
    }

    public static String ocr2txtKeep(File imageFile, int left, int top, int width, int height) {
        return getTxtFromUrl(getOcrUrlKeep(), imageFile, left, top, width, height);
    }

    private static String getTxtFromUrl(String url, File imageFile, int left, int top, int width, int height) {
        // 创建HTTP客户端
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建POST请求
            HttpPost httpPost = new HttpPost(url);

            // 根据范围截取图片
            log.info("正在裁剪图片：{}，{}，{}，{}，{}", imageFile.getAbsolutePath(), left, top, width, height);
            byte[] bytes = BQImageUtils.cropImage(imageFile, left, top, width, height);

            // 使用 MultipartEntityBuilder 构建多部分表单
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addBinaryBody("file", bytes, ContentType.DEFAULT_BINARY, imageFile.getName())
                    .build();

            // 设置请求实体
            return getStringBody(httpClient, httpPost, entity);
        } catch (IOException e) {
            log.error("ocr2txtOnly occurred while making HTTP request: {}", e.getMessage());
            throw new BQApiException("Error occurred while making HTTP request");
        }
    }

    private static String getStringBody(CloseableHttpClient httpClient, HttpPost httpPost, HttpEntity entity) throws IOException {
        httpPost.setEntity(entity);

        return httpClient.execute(httpPost, httpResponse -> {
            int statusCode = httpResponse.getCode();
            if (statusCode >= 200 && statusCode < 300) {
                String body = EntityUtils.toString(httpResponse.getEntity());
                log.info("ocr2txtOnly response body: {}", body);
                if (Objects.nonNull(body) && StringUtils.trim(body).isEmpty()) {
                    // 返回空字符串时，以null返回
                    body = null;
                }
                // 解析响应实体为字符串
                return body;
            } else {
                throw new RuntimeException("Unexpected response status: " + statusCode);
            }
        });
    }

    public static void main(String[] args) {
        String imagePath = "D:\\Wcke\\000SourceCode\\VSCodeProjects\\mfsms-client\\public\\images\\1101.png";
        String response = ocr2txtKeep(imagePath, 212, 183, 161, 33);
        System.out.println(response);

//        String response = ocr2txt(imagePath);
//        System.out.println(response);
    }
}
