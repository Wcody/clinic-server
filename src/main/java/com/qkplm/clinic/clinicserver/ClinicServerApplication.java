/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.qkplm.clinic.clinicserver.mp.BQDataPermissionHandlerCustom;
import com.qkplm.clinic.libcommon.mybatis.BQDataPermissionHandler;
import com.qkplm.clinic.libcommon.utils.BQAuthUtils;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-11 18:10
 */
@SpringBootApplication(scanBasePackages = {"com.qkplm.clinic.libcommon", "com.qkplm.clinic.clinicserver"}, exclude = RabbitAutoConfiguration.class)
@EnableScheduling
public class ClinicServerApplication {

    public static void main(String[] args) {
        // 初始化授权组件
        BQAuthUtils.initAuth();
        // 先初始化权限管理组件
        BQDataPermissionHandler.initHandlerCustom(new BQDataPermissionHandlerCustom());
        // 启动程序
        SpringApplication.run(ClinicServerApplication.class, args);
    }
}
