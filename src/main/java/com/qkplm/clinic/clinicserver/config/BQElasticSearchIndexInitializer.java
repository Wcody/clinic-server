/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2025-05-05 11:45
 */
@Slf4j
//@Component
public class BQElasticSearchIndexInitializer {
    private final ElasticsearchClient client;

    public BQElasticSearchIndexInitializer(ElasticsearchClient client) {
        this.client = client;
    }

    @PostConstruct
    public void init() throws IOException {
        boolean exists = client.indices().exists(e -> e.index(BQElasticsearchConfig.RECORD_INDEX_NAME)).value();
        if (exists) {
            log.info("✅ Index already exists: {}", BQElasticsearchConfig.RECORD_INDEX_NAME);
            return;
        }

        log.info("📦 Creating index: {}", BQElasticsearchConfig.RECORD_INDEX_NAME);

        client.indices().create(c -> c
                .index(BQElasticsearchConfig.RECORD_INDEX_NAME)
                .settings(s -> s
                        .analysis(a -> a
                                .analyzer("ik_max_analyzer", analyzer -> analyzer
                                        .custom(ca -> ca
                                                .tokenizer("ik_max_word")
                                        )
                                )
                        )
                )
                .mappings(m -> m
                        .properties("title", p -> p
                                .text(t -> t
                                        .analyzer("ik_max_word")
                                        .searchAnalyzer("ik_smart")
                                )
                        )
                        .properties("author", p -> p
                                .text(t -> t
                                        .analyzer("ik_max_word")
                                        .searchAnalyzer("ik_smart")
                                )
                        )
                )
        );

        log.info("✅ Index created with IK analyzer.");
    }
}
