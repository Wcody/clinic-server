/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import com.qkplm.clinic.libcommon.entity.BQFullTextEntity;
import com.qkplm.clinic.libcommon.entity.BQOcrEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import com.qkplm.clinic.clinicserver.config.BQRabbitConfig;
import com.qkplm.clinic.clinicserver.entity.BqLogEntity;

import java.util.Objects;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2025-04-25 10:37
 */
@Slf4j
public class BQRabbitUtils {
    static private AmqpTemplate rabbitTemplate;
    /**
     * 初始化，只调用一次
     */
    public static void init(AmqpTemplate rabbitTemplate) {
        if (Objects.isNull(BQRabbitUtils.rabbitTemplate)) {
            log.info("rabbitTemplate is init");
            BQRabbitUtils.rabbitTemplate = rabbitTemplate;
        }
    }

    /**
     * 检查初始化
     */
    public static void checkInit() {
        if (Objects.isNull(rabbitTemplate)) {
            log.error("rabbitTemplate is null");
            throw new RuntimeException("rabbitTemplate is null");
        }
    }

    /**
     * 发送操作日志消息
     */
    public static void sendOplog(BqLogEntity logEntity) {
        try {
            checkInit();
            String message = BQJacksonUtils.toJson(logEntity);
            log.info("send oplog message: {}", message);
            rabbitTemplate.convertAndSend(BQRabbitConfig.OPLOG_QUEUE_NAME, message);
        } catch (Exception e) {
            log.error("sendOplog message error: {}", e.getMessage());
        }
    }

    /**
     * 发送OCR消息
     */
    public static void sendOcr(BQOcrEntity ocrEntity) {
        try {
            checkInit();
            String message = BQJacksonUtils.toJson(ocrEntity);
            log.info("send ocr message: {}", message);
            rabbitTemplate.convertAndSend(BQRabbitConfig.OCR_QUEUE_NAME, message);
        } catch (Exception e) {
            log.error("sendOcr error: {}", e.getMessage());
        }
    }

    /**
     * 发送OCR消息
     */
    public static void sendFulltext(BQFullTextEntity fullTextEntity) {
        try {
            checkInit();
            String message = BQJacksonUtils.toJson(fullTextEntity);
            log.info("send fulltext message: {}", message);
            rabbitTemplate.convertAndSend(BQRabbitConfig.FULLTEXT_QUEUE_NAME, message);
        } catch (Exception e) {
            log.error("sendFulltext error: {}", e.getMessage());
        }
    }
}
