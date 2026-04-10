/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-14 23:01
 */
public class BQJwtUtils {
    /**
     * 默认JWT请求头
     */
    public static final String HEADER = "Authorization";
    /**
     * JWT配置信息
     */
    private static BQJwtConfig jwtConfig;

    /**
     * 私有构造方法
     */
    private BQJwtUtils() {
    }

    /**
     * 初始化参数
     */
    public static void initialize(String header, String tokenHead, String issuer, String secretKey, long expirationTime, List<String> issuers, String audience) {
        jwtConfig = new BQJwtConfig();
        jwtConfig.setHeader(StringUtils.isNotBlank(header) ? header : HEADER);
        jwtConfig.setTokenHead(tokenHead);
        jwtConfig.setIssuer(issuer);
        jwtConfig.setSecretKey(secretKey);
        jwtConfig.setExpirationTime(expirationTime);
        if (CollectionUtils.isEmpty(issuers)) {
            issuers = Collections.singletonList(issuer);
        }
        jwtConfig.setIssuers(issuers);
        jwtConfig.setAudience(audience);
        jwtConfig.setAlgorithm(Algorithm.HMAC256(jwtConfig.getSecretKey()));
    }

    /**
     * 初始化参数
     */
    public static void initialize(String header, String issuer, String secretKey, long expirationTime) {
        initialize(header, null, issuer, secretKey, expirationTime, null, null);
    }

    /**
     * 初始化参数
     */
    public static void initialize(String header, String tokenHead, String issuer, String secretKey, long expirationTime) {
        initialize(header, tokenHead, issuer, secretKey, expirationTime, null, null);
    }

    /**
     * 生成 Token
     */
    public static String generateToken(String subject) {
        return generateToken(subject, jwtConfig.getExpirationTime());
    }

    public static String generateToken(String subject, String issuer) {
        return generateToken(subject, issuer, jwtConfig.getExpirationTime());
    }

    /**
     * 生成 Token
     */
    public static String generateToken(String subject, long expirationTime) {
        return generateToken(subject, jwtConfig.getIssuer(), expirationTime);
    }

    /**
     * 生成 Token
     */
    public static String generateToken(String subject, String issuer, long expirationTime) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime * 1000);

        return JWT.create()
                .withSubject(subject)
                .withIssuer(issuer)
                .withAudience(jwtConfig.getAudience())
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .sign(jwtConfig.getAlgorithm());
    }

    /**
     * 获取 Token 内容
     */
    public static String getTokenContent(String token) {
        if (StringUtils.isNotBlank(jwtConfig.getTokenHead())) {
            token = token.substring(jwtConfig.getTokenHead().length()).trim();
        }
        return token;
    }

    /**
     * 判断 Token 是否有效
     */
    public static boolean isValidToken(String token) {
        try {
            token = getTokenContent(token);
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            // Token验证失败
            return false;
        }
    }

    /**
     * 判断 Token 是否过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            token = getTokenContent(token);
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);

            Date expirationDate = JWT.decode(token).getExpiresAt();
            return expirationDate != null && expirationDate.before(new Date());
        } catch (JWTVerificationException exception) {
            // Token验证失败
            return false;
        }
    }

    /**
     * 获取主题内容，这里表示登录账号名
     */
    public static String getSubject(String token) {
        token = getTokenContent(token);
        return JWT.decode(token).getSubject();
    }

    /**
     * 获取发行者，这里表示后续缓存地址，格式如下：[登录平台]:[redisId]
     */
    public static String getIssuer(String token) {
        token = getTokenContent(token);
        return JWT.decode(token).getIssuer();
    }

    /**
     * 获取当前配置
     */
    public static BQJwtConfig getCurrentConfig() {
        return jwtConfig;
    }

    @Data
    public static class BQJwtConfig {
        /**
         * JwtToken Header标签
         */
        private String header;
        /**
         * Token头
         */
        private String tokenHead;
        /**
         * 签发者
         */
        private String issuer;
        /**
         * 密钥
         */
        private String secretKey;
        /**
         * Token 过期时间
         */
        private long expirationTime;
        /**
         * 签发者列表
         */
        private List<String> issuers;
        /**
         * 接受者
         */
        private String audience;
        /**
         * 加密算法
         */
        private Algorithm algorithm;
    }
}
