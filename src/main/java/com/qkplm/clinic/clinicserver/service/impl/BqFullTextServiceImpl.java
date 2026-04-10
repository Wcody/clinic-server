/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;
import com.qkplm.clinic.clinicserver.service.IBqFullTextService;

/**
 * @author: Wcke
 * @description: 全文索引服务实现类
 * @datetime: 2025-04-27 14:51
 */
@Slf4j
@Service
public class BqFullTextServiceImpl implements IBqFullTextService {
    private final ElasticsearchClient elasticsearchClient;
    // 病案全文索引名称
    private static final String CONTENT_INDEX_NAME = "RecordContexts";

    public BqFullTextServiceImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }


    @Override
    public boolean saveRecord(TbRecordEntity entity) {
        try {
            elasticsearchClient.index(index -> index
                    .index(CONTENT_INDEX_NAME)
                    .id(entity.getEid())
                    .document(entity)
            );
            return true;
        } catch (Exception e) {
            log.error("保存全文索引失败: {}", e.getMessage());
        }
        return false;
    }
}
