/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2025-04-25 10:16
 */
//@Configuration
public class BQRabbitConfig {
    // 操作日志队列名称
    public static final String OPLOG_QUEUE_NAME = "oplog_queue";

    // OCR队列名称
    public static final String OCR_QUEUE_NAME = "oplog_queue";

    // 全文索引队列名称
    public static final String FULLTEXT_QUEUE_NAME = "fulltext_queue";

    /**
     * 操作日志队列
     */
    //@Bean
    public Queue oplogQueue() {
        return new Queue(OPLOG_QUEUE_NAME, true);
    }

    /**
     * OCR队列
     */
    //@Bean
    public Queue ocrQueue() {
        return new Queue(OCR_QUEUE_NAME, true);
    }

    /**
     * 全文索引队列
     */
    //@Bean
    public Queue fulltextQueue() {
        return new Queue(FULLTEXT_QUEUE_NAME, true);
    }
}
