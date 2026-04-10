/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.qkplm.clinic.clinicserver.config.BQRabbitConfig;

/**
 * @author: Wcke
 * @description: 操作日志消费者
 * @datetime: 2025-04-25 14:13
 */
@Slf4j
@Component
public class BQOplogConsumer {
    /**
     * 操作日志消费者A
     */
    @RabbitListener(queues = BQRabbitConfig.OPLOG_QUEUE_NAME)
    public void oplogA(String message) {
        log.info("[Consumer oplogA] Received: {}", message);
    }

    /**
     * 操作日志消费者B
     */
    @RabbitListener(queues = BQRabbitConfig.OPLOG_QUEUE_NAME)
    public void oplogB(String message) {
        log.info("[Consumer oplogB] Received: {}", message);
    }
}
