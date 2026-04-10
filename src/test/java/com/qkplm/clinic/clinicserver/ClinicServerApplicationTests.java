/*
 * 版权声明 Copyright (c) 2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.qkplm.clinic.clinicserver.entity.TbUserEntity;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;
import com.qkplm.clinic.libcommon.utils.BQRSAUtils;

import java.time.LocalDateTime;

@SpringBootTest
class ClinicServerApplicationTests {

    @Test
    void contextLoads() {
        TbUserEntity user = new TbUserEntity();
        user.setName("baqi");
        user.setPassword("123456");
        user.setPhone("123456789");
        user.setAccount("123456");
        user.setEid("123456");
        user.setCreatedBy("123456");
        user.setUpdatedBy("123456");
        user.setVersion(1);
        user.setCreatedTime(LocalDateTime.now());
        System.out.println(BQJacksonUtils.toJson(user));
    }

    @Test
    void test() {
        QueryWrapper<TbUserEntity> queryWrapper = new QueryWrapper<>();

        // 动态条件拼接
        queryWrapper.and(wrapper -> wrapper.eq("name", "John").ne("version", 25))
                .or(wrapper -> wrapper.gt("created_time", "2022-01-01").le("updated_time", "2023-01-01"));

        // 打印SQL语句
        System.out.println("SQL: " + queryWrapper.getSqlSelect());
    }

    @Test
    void test1() throws Exception {
        String plainText = "Hello, World!电视剧发货时间洞口防护较大刷卡合计dds";
        System.out.println("Plain Text: " + plainText);
        String encryptedText = BQRSAUtils.encryptWithPublicKey(plainText, BQRSAUtils.getPublicKeyFile().toString());
        System.out.println("Encrypted Text: " + encryptedText);
        String decryptedText = BQRSAUtils.decryptWithPrivateKey(encryptedText, BQRSAUtils.getPrivateKeyFile().toString());
        System.out.println("Decrypted Text: " + decryptedText);

        encryptedText = BQRSAUtils.encryptWithPrivateKey(plainText, BQRSAUtils.getPrivateKeyFile().toString());
        System.out.println("Encrypted Text: " + encryptedText);
        decryptedText = BQRSAUtils.decryptWithPublicKey(encryptedText, BQRSAUtils.getPublicKeyFile().toString());
        System.out.println("Decrypted Text: " + decryptedText);
    }
}
