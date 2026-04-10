/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import com.qkplm.clinic.clinicserver.entity.BqParamItemEntity;
import com.qkplm.clinic.clinicserver.entity.TbKindEntity;
import com.qkplm.clinic.clinicserver.mapper.TbKindMapper;
import com.qkplm.clinic.clinicserver.service.IBqParamItemService;
import com.qkplm.clinic.clinicserver.service.ITbAttachmentService;
import com.qkplm.clinic.clinicserver.service.ITbKindService;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
* @author Wcke
* @description <p>档案分类 服务接口类</p>
* @datetime 2024-7-12 9:43
*/
@Slf4j
@Service
public class TbKindServiceImpl extends BaqiServiceImpl<TbKindMapper, TbKindEntity> implements ITbKindService {
    private final IBqParamItemService paramItemService;
    private final ITbAttachmentService attachmentService;

    public TbKindServiceImpl(IBqParamItemService paramItemService, @Lazy ITbAttachmentService attachmentService) {
        this.paramItemService = paramItemService;
        this.attachmentService = attachmentService;
    }

    @Override
    public Object setStatus(String eid, Boolean status) {
        TbKindEntity kind = getById(eid);
        if (Objects.nonNull(kind)) {
            kind.setStatus(status);
            return updateById(kind);
        }
        throw new BQApiException("目标不存在");
    }

    @Override
    public List<TbKindEntity> listQt(HashMap<String, Object> params) {
        log.info("params:{}", BQJacksonUtils.toJson(params));
        // 这个参数是必须指定的
        Integer kind = (Integer) params.get("kind");
        if (Objects.isNull(kind)) {
            throw new BQApiException("kind不能为空");
        }

        if (kind == -1) {
            log.warn("listQt的kind参数非法，当前值为：{}", kind);
            return new ArrayList<>();
        }

        Integer paramId = List.of(16, 14, 15).get(kind);
        BqParamItemEntity paramItem = paramItemService.getById(paramId);
        if (Objects.isNull(paramItem)) {
            throw new BQApiException("齐套性参数项不存在");
        }

        ObjectNode paramValue = paramItemService.getRealParamData(paramItem.getId());
        //ObjectNode paramValue = paramItem.getParamValue();
        if (Objects.isNull(paramValue)) {
            throw new BQApiException("齐套性参数项值不存在");
        }

        log.info("paramValue:{}", BQJacksonUtils.toJson(paramValue));
        JsonNode node = paramValue.get("status");
        if (Objects.isNull(node) || !node.asBoolean()) {
            log.info("齐套性参数未开启");
            return new ArrayList<>();
        }

        JsonNode kindListNode = paramValue.get("kindList");
        if (Objects.isNull(kindListNode) || (kindListNode.isArray() && kindListNode.isEmpty())) {
            log.info("齐套性规则参数未设置");
            return new ArrayList<>();
        }

        String recordId = (String) params.get("recordId");
        if (StringUtils.isBlank(recordId)) {
            log.info("没有指定判断齐套性的病案ID");
            return new ArrayList<>();
        }

        // 不存在的
        ArrayList<String> notExists = new ArrayList<>();

        // 根据kindId获取已经存在的
        List<String> kindIds = attachmentService.getKindIdsByRecordId(recordId);

        // 查找不存在的列表
        for (int i = 0; i < kindListNode.size(); i++) {
            String id = kindListNode.get(i).asText();
            boolean bFound = false;
            for (String kindId : kindIds) {
                if (Objects.equals(id, kindId)) {
                    bFound = true;
                    break;
                }
            }
            if (!bFound) {
                // 存在列表中还不存在
                notExists.add("'" + id + "'");
            }
        }

        if (notExists.isEmpty()) {
            log.info("齐套性规则检验全部通过");
            return new ArrayList<>();
        }

        // 构造sql
        String sql = "select * from tb_kind where eid in (" + String.join(", ", notExists) +
                ")";
        log.info("listQt sql:{}", sql);
        return baseMapper.listQt(sql);
    }
}
