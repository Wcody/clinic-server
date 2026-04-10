/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkplm.clinic.libcommon.api.BQApiResult;
import com.qkplm.clinic.libcommon.api.BQResult;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;
import com.qkplm.clinic.libcommon.utils.BQRedisUtils;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.Duration;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-12 17:58
 */
@Slf4j
@RestControllerAdvice
public class BQRestResponseAdvice implements ResponseBodyAdvice<Object> {
    private final ObjectMapper mapper = BQJacksonUtils.newObjectMapper();

    /**
     * 响应数据封装
     */
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object o
            , @NonNull MethodParameter methodParameter
            , @NonNull MediaType mediaType
            , @NonNull Class<? extends HttpMessageConverter<?>> aClass
            , @NonNull ServerHttpRequest serverHttpRequest
            , @NonNull ServerHttpResponse serverHttpResponse) {
        return switch (o) {
            case BQResult<?> bqResult ->
                //直接返回对象
                bqResult.getData();
            case BQApiResult bqApiResult -> bqApiResult;
            case String string ->
                //如果是字符串，等封装后再返回字符串
                mapper.writeValueAsString(BQApiResult.builder().data(string).build());
            case null, default -> BQApiResult.builder().data(o).build();
        };
    }

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter,@NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * 配置ObjectMapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return BQJacksonUtils.newObjectMapper();
    }

    /**
     * 配置Tomcat
     */
    @Bean
    @Primary
    public TomcatServletWebServerFactory tomcatEmbedded() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(connector -> {
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
                //-1 means unlimited
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
            //支持特殊字符
            connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
            connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
        });
        return tomcat;
    }

    /**
     * 配置RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder templateBuilder) {
        //超时设置
        templateBuilder.setConnectTimeout(Duration.ofDays(2000)).setReadTimeout(Duration.ofDays(500));
        return templateBuilder.build();
    }

    /**
     * 配置redisTemplate
     */
    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(BQJacksonUtils.newObjectMapper(true));
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.afterPropertiesSet();
        // 初始化Redis工具类
        BQRedisUtils.init(redisTemplate);
        return redisTemplate;
    }

    /**
     * 配置rabbitTemplate
     */
//    @Bean
//    public AmqpTemplate rabbitTemplate() {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate();
//        return rabbitTemplate;
//    }
}
