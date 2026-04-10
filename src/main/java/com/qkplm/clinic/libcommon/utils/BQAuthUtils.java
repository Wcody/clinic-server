/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qkplm.clinic.libcommon.entity.BQAuthInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.qkplm.clinic.clinicserver.entity.TbMenuEntity;
import com.qkplm.clinic.libcommon.api.BQApiException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * @author: Wcke
 * @description: 系统授权相关工具类
 * @datetime: 2024-10-26 20:21
 */
@Slf4j
public class BQAuthUtils {
    private static final Object lock = new Object();
    private static final String AUTH_PATH = "auth";
    private static String DEVICE_CODE;
    private static ObjectNode AUTH_INFO;
    private static List<TbMenuEntity> MENUS;
    private static List<String> MENU_IDS;

    private static Path getAuthFilePath() {
        String defaultPath = Paths.get(System.getProperty("user.dir"), AUTH_PATH).toString();
        return Path.of(BQIOUtils.getAppPathIfExists(AUTH_PATH, defaultPath), "auth.bqa");
    }

    public static ObjectNode getSysInfo() {
        try {
            if (Objects.nonNull(AUTH_INFO)) {
                return AUTH_INFO;
            } else {
                synchronized (lock) {
                    if (!Objects.isNull(AUTH_INFO)) {
                        return AUTH_INFO;
                    }
                    Path path = getAuthFilePath();
                    log.info("授权文件路径: {}", path);
                    if (!Files.exists(path)) {
                        return null;
                    }
                    // 获取私钥加密的内容
                    byte[] bytes = Files.readAllBytes(path);
                    // 使用公钥解密
                    String text = BQRSAUtils.decryptWithPublicKey(bytes, BQRSAUtils.getPublicKeyFile().toString());
                    AUTH_INFO = (ObjectNode) BQJacksonUtils.jsonTo(text);
                    MENUS = BQJacksonUtils.getMapper().treeToValue(AUTH_INFO.get("menus"), new TypeReference<>() {
                    });
                    MENU_IDS = MENUS.stream().map(TbMenuEntity::getEid).toList();
                    return AUTH_INFO;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void initAuth() {
        try {
            DEVICE_CODE = BQDeviceUtils.getDeviceCode();
            log.info("设备编码: {}", DEVICE_CODE);
            getSysInfo();
        } catch (Exception e) {
            log.error("初始化授权信息失败", e);
        }
    }

    private static JsonNode getNodeByName(JsonNode jsonNode, String name) {
        JsonNode node = jsonNode.get(name);
        if (Objects.isNull(node)) {
            throw new RuntimeException("授权文件无效");
        }
        return node;
    }

    public static String textNodeByName(JsonNode jsonNode, String name) {
        JsonNode node = jsonNode.get(name);
        if (Objects.isNull(node)) {
            return "";
        }
        return node.asText();
    }

    public static boolean checkAuth() {
        if (BQIOUtils.isDevMode()) {
            return true;
        }
        try {
            ObjectNode auth = getSysInfo();
            if (Objects.isNull(auth)) {
                throw new Exception("授权文件不存在");
            }
            JsonNode customer = getNodeByName(auth, "customer");
            JsonNode deviceCode = getNodeByName(customer, "deviceCode");
            if (!StringUtils.equalsIgnoreCase(DEVICE_CODE, deviceCode.asText())) {
                log.info("本机设备码: {}", DEVICE_CODE);
                log.info("授权文件设备码: {}", deviceCode.asText());
                throw new Exception("授权信息比对不通过");
            }
        } catch (Exception e) {
            log.error("授权信息校验失败", e);
            throw new BQApiException(9001, "授权信息无效");
        }
        return true;
    }

    public static BQAuthInfoEntity getSysAuthEntity() {
        ObjectNode sysInfo = getSysInfo();
        if (Objects.nonNull(sysInfo)) {
            return BQAuthInfoEntity.builder()
                    .clientId(sysInfo.get("customer").get("eid").asText())
                    .clientName(sysInfo.get("customer").get("name").asText())
                    .authType(sysInfo.get("customer").get("authType").asInt())
                    .expireDate(sysInfo.get("customer").get("expireDate").asText())
                    .maxTenantCount(sysInfo.get("customer").get("maxTenantCount").asInt())
                    .maxUserCount(sysInfo.get("customer").get("maxUserCount").asInt())
                    .regCode(textNodeByName(sysInfo, "regCode")).build();
        } else {
            return null;
        }
    }

    public static String getClientName() {
        ObjectNode sysInfo = getSysInfo();
        if (Objects.nonNull(sysInfo)) {
            return sysInfo.get("customer").get("name").asText();
        } else {
            return "无效授权用户";
        }
    }

    public static List<TbMenuEntity> getMenus() {
        return MENUS;
    }

    public static List<String> getMenuIds() {
        return MENU_IDS;
    }

    public static boolean isAuthMenu(String menuId) {
        return MENU_IDS.contains(menuId);
    }
}
