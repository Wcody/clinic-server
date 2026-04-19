/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Objects;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-12 19:35
 */
@Slf4j
public class BQJacksonUtils {
    @Getter
    private static final ObjectMapper mapper = newObjectMapper();

    public static ObjectMapper newObjectMapper() {
        return newObjectMapper(false);
    }
    public static ObjectMapper newObjectMapper(boolean enableDefaultTyping) {
        ObjectMapper mapper = new ObjectMapper();
        //这个配置用于带类型的序列化，常用于RPC或跨端的序列化
        if (enableDefaultTyping) {
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        }
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        //不能序列化空对象
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //不将date转化成timestamp
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //忽略未知属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //不将空转化为null
        mapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        //允许没有引号的字段名（非标准）
        mapper.disable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        //增加日期支持
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // LocalDateTime 序列化器（使用标准格式）
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // LocalDateTime 反序列化器（支持多种格式，包括 ISO 8601）
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String dateStr = p.getText();
                if (dateStr == null || dateStr.isEmpty()) {
                    return null;
                }
                
                try {
                    // 尝试解析 ISO 8601 格式（带时区，如 2026-04-13T06:41:43.454Z）
                    if (dateStr.contains("T") && (dateStr.endsWith("Z") || dateStr.contains("+") || dateStr.lastIndexOf("-") > 10)) {
                        Instant instant = Instant.parse(dateStr);
                        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    }
                    // 尝试解析标准格式 yyyy-MM-dd HH:mm:ss
                    else if (dateStr.contains(" ")) {
                        return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }
                    // 尝试解析 ISO 本地格式 yyyy-MM-ddTHH:mm:ss
                    else if (dateStr.contains("T")) {
                        return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    }
                    // 默认尝试直接解析
                    else {
                        return LocalDateTime.parse(dateStr);
                    }
                } catch (Exception e) {
                    log.warn("Failed to parse date string: {}", dateStr, e);
                    throw new IOException("Failed to parse date: " + dateStr, e);
                }
            }
        });
        
        javaTimeModule.addSerializer(LocalDate.class,new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDate.class,new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        mapper.registerModule(javaTimeModule);

        return mapper;
    }

    public static ObjectNode newObjectNode() {
        return mapper.createObjectNode();
    }

    public static ArrayNode newArrayNode() {
        return mapper.createArrayNode();
    }

    public static <T> String toJson(T t) {
        return toJson(t, null);
    }

    public static JsonNode jsonTo(String json) {
        return jsonTo(json, new TypeReference<JsonNode>() {
        });
    }

    public static <T> T jsonTo(String json, TypeReference<T> typeReference) {
        return jsonTo(json, typeReference, null);
    }

    public static <T> T jsonToBean(String json, Class<T> clazz) {
        return jsonToBean(json, clazz, null);
    }

    public static <T> T fileToBean(File file, Class<T> clazz) {
        return fileToBean(file, clazz, null);
    }

    public static <T> String toJson(T t, ObjectMapper objectMapper) {
        if (Objects.isNull(t))
            return null;
        try {
            return (objectMapper == null ? mapper : objectMapper).writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.error("json to bean error", e);
            return null;
        }
    }

    public static <T> T jsonToBean(String json, Class<T> clazz, ObjectMapper objectMapper) {
        if (StringUtils.isBlank(json))
            return null;
        try {
            return objectMapper == null ? mapper.readValue(json, clazz) : objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("json to bean error", e);
            return null;
        }
    }

    public static <T> T jsonToBean(String json, TypeReference<T> valueTypeRef) {
        return jsonToBean(json, valueTypeRef, null);
    }

    public static <T> T jsonToBean(String json, TypeReference<T> valueTypeRef, ObjectMapper objectMapper) {
        if (StringUtils.isBlank(json))
            return null;
        try {
            return objectMapper == null ? mapper.readValue(json, valueTypeRef) : objectMapper.readValue(json, valueTypeRef);
        } catch (IOException e) {
            log.error("json to bean error", e);
            return null;
        }
    }

    public static <T> T fileToBean(File file, Class<T> clazz, ObjectMapper objectMapper) {
        if (!file.exists())
            return null;
        try {
            return objectMapper == null ? mapper.readValue(file, clazz) : objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            log.error("file to bean error", e);
            return null;
        }
    }

    public static <T> T jsonTo(String json, TypeReference<T> typeReference, ObjectMapper objectMapper) {
        if (StringUtils.isBlank(json))
            return null;
        try {
            return objectMapper == null ? mapper.readValue(json, typeReference) : objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            log.error("json to bean error", e);
            return null;
        }
    }

    public static <T> T jsonTo(String json, Class<T> clazz, ObjectMapper objectMapper) {
        if (StringUtils.isBlank(json))
            return null;
        try {
            return mapper.readValue(json, new TypeReference<T>() {
            });
        } catch (Exception e) {
            log.error("json to bean error", e);
            return null;
        }
    }

}
