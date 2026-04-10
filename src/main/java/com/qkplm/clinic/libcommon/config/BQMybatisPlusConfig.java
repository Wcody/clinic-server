/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.qkplm.clinic.libcommon.mybatis.BQDataPermissionHandler;
import com.qkplm.clinic.libcommon.mybatis.BQMapperMethodsInjector;
import com.qkplm.clinic.libcommon.mybatis.BQTenantHandler;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-27 07:14
 */
@Component
public class BQMybatisPlusConfig implements CommandLineRunner {
    private final BQTenantHandler tenantHandler;

    public BQMybatisPlusConfig(BQTenantHandler tenantHandler) {
        this.tenantHandler = tenantHandler;
    }

    @Override
    public void run(String... args) throws Exception {
        // 注入自建的ObjectMapper，否则报JAVA8 JSR310日期处理异常
        // 也需要开启@TableName(autoResultMap = true)！！！，否则返回对象的字段为空
        JacksonTypeHandler.setObjectMapper(BQJacksonUtils.newObjectMapper());
    }

    /**
     * 按需注入插件
     */
    @Bean
    @Primary
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 注入非法SQL拦截插件
        // interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        // 注入乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 注入多租户插件
        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
        tenantInterceptor.setTenantLineHandler(tenantHandler);
        interceptor.addInnerInterceptor(tenantInterceptor);
        // 注入数据权限插件
        DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor(new BQDataPermissionHandler());
        interceptor.addInnerInterceptor(dataPermissionInterceptor);
        // 最后注入分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 注入自定义方法
     */
    @Bean
    @Primary
    public ISqlInjector iSqlInjector() {
        return new BQMapperMethodsInjector();
    }
}
