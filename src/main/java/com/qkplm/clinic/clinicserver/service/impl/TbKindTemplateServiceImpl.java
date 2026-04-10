/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.qkplm.clinic.clinicserver.entity.TbKindTemplateEntity;
import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;
import com.qkplm.clinic.clinicserver.mapper.TbKindTemplateMapper;
import com.qkplm.clinic.clinicserver.service.ITbKindTemplateService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;
import com.qkplm.clinic.libcommon.utils.BQOcrUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
* @author Wcke
* @description <p>分类模板 服务接口类</p>
* @datetime 2024-7-12 9:43
*/
@Slf4j
@Service
public class TbKindTemplateServiceImpl extends BaqiServiceImpl<TbKindTemplateMapper, TbKindTemplateEntity> implements ITbKindTemplateService {

    @Override
    public List<TbKindTemplateEntity> getAllList() {
        LambdaQueryWrapper<TbKindTemplateEntity> wrapper = new QueryWrapper<TbKindTemplateEntity>().lambda();
        wrapper.ge(TbKindTemplateEntity::getImgSize, 0);
        return this.list(wrapper);
    }

    @Override
    public void handleOcr(File imageFile) {
        handleOcr(imageFile, getAllList());
    }

    @Override
    public int handleOcr(File imageFile, List<TbKindTemplateEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            TbKindTemplateEntity entity = list.get(i);
            if (handleOcr(imageFile, entity)) {
                log.info("匹配索引{}", i);
                log.info("{} 匹配成功", entity);
                return i;
            };
        }
        return -1;
    }

    @Override
    public void handleOcr(String imagePath) {
        handleOcr(new File(imagePath), getAllList());
    }

    @Override
    public int handleOcr(String imagePath, List<TbKindTemplateEntity> list) {
        return handleOcr(new File(imagePath), list);
    }

    @Override
    public boolean handleOcr(String imagePath, TbKindTemplateEntity entity) {
        return handleOcr(new File(imagePath), entity);
    }

    @Override
    public boolean handleOcr(File imageFile, TbKindTemplateEntity entity) {
        // 匹配分类
        ArrayNode arrayNode = entity.getRegKeys();
        if (Objects.nonNull(arrayNode) && !arrayNode.isEmpty()) {
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode node = arrayNode.get(i);
                if (node.isObject()) {
                    ObjectNode objectNode = (ObjectNode) node;
                    JsonNode keysNode = objectNode.get("keys");
                    ArrayNode keysArrayNode = (ArrayNode) keysNode;
                    if (Objects.nonNull(keysArrayNode) && !keysArrayNode.isEmpty()) {
                        int x = objectNode.get("x").asInt();
                        int y = objectNode.get("y").asInt();
                        int w = objectNode.get("w").asInt();
                        int h = objectNode.get("h").asInt();
                        // 获取图片内容
                        String text = BQOcrUtils.ocr2txtOnly(imageFile, x, y, w, h);
                        if (!StringUtils.isEmpty(text)) {
                            // 遍历关键字是否存在
                            for (int j = 0; j < keysArrayNode.size(); j++) {
                                String key = keysArrayNode.get(j).asText();
                                if (!text.contains(key)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public TbRecordEntity handleOcr4Record(File imageFile, TbKindTemplateEntity entity) {
        // 提取字段信息
        ArrayNode arrayNode = entity.getDataFields();
        if (Objects.nonNull(arrayNode) && !arrayNode.isEmpty()) {
            TbRecordEntity record = new TbRecordEntity();
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode node = arrayNode.get(i);
                if (node.isObject()) {
                    ObjectNode objectNode = (ObjectNode) node;
                    String fieldName = objectNode.get("enFieldName").asText();
                    if (Objects.nonNull(fieldName)) {
                        int x = objectNode.get("x").asInt();
                        int y = objectNode.get("y").asInt();
                        int w = objectNode.get("w").asInt();
                        int h = objectNode.get("h").asInt();
                        // 获取图片内容
                        String text = BQOcrUtils.ocr2txtOnly(imageFile, x, y, w, h);
                        // 赋值
                        record.set(fieldName, text);
                    }
                }
            }
            return record;
        }
        return null;
    }
}
