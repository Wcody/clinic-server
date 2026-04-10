/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.mysql;

import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiMapper;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;
import com.qkplm.clinic.libcommon.utils.BQFileUtils;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-11 18:48
 */
public class BQClinicServerCodeGenerator {
    private static void generateEidBaseEntity(String url, String outDir, String dbUser, String dbPassword, String parentPackage) {
        FastAutoGenerator.create(url, dbUser, dbPassword)
                .globalConfig(builder -> builder
                        .author("Wcke")
                        .outputDir(outDir)
                        .commentDate("yyyy-MM-dd")
                        .disableOpenDir()
                )
                .packageConfig(builder -> builder
                        .parent(parentPackage)
                        .controller("controller")
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                )
                .strategyConfig(builder -> builder
                                .likeTable(new LikeTable("tb_", SqlLike.RIGHT))
                                .controllerBuilder()
                                .enableRestStyle()
                                //.enableFileOverride()
                                .enableHyphenStyle()
                                .entityBuilder()
                                .enableLombok()
                                .enableFileOverride()
                                .disableSerialVersionUID()
                                .formatFileName("%sEntity")
                                .superClass(BQEidBaseEntity.class)
                                .addSuperEntityColumns("createdTime", "updatedTime", "createdBy", "updatedBy", "version", "deleted", "eid", "tenantId")
                                .logicDeleteColumnName("deleted")
                                .versionColumnName("version")
                                .addIgnoreColumns("uid")
                                //.addTableFills(
                                //        new Column("password", FieldFill.INSERT)
                                //)
                                .mapperBuilder()
                                .superClass(IBaqiMapper.class)
                                //.enableFileOverride()
                                .mapperAnnotation(Mapper.class)
                                .serviceBuilder()
                                .superServiceClass(IBaqiService.class)
                                .superServiceImplClass(BaqiServiceImpl.class)
                                //.enableFileOverride()

                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    private static void generateIdBaseEntity(String url, String outDir, String dbUser, String dbPassword, String parentPackage) {

        FastAutoGenerator.create(url, dbUser, dbPassword)
                .globalConfig(builder -> builder
                        .author("Wcke")
                        .outputDir(outDir)
                        .commentDate("yyyy-MM-dd")
                        .disableOpenDir()
                )
                .packageConfig(builder -> builder
                        .parent(parentPackage)
                        .controller("controller")
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                )
                .strategyConfig(builder -> builder
                                .likeTable(new LikeTable("bq_", SqlLike.RIGHT))
                                .controllerBuilder()
                                .enableRestStyle()
                                //.enableFileOverride()
                                .enableHyphenStyle()
                                .entityBuilder()
                                .enableLombok()
                                .enableFileOverride()
                                .disableSerialVersionUID()
                                .formatFileName("%sEntity")
                                .superClass(BQIdBaseEntity.class)
                                .addSuperEntityColumns("createdTime", "updatedTime", "createdBy", "updatedBy", "id", "tenantId")
                                //.addIgnoreColumns("uid")
                                //.addTableFills(
                                //        new Column("password", FieldFill.INSERT)
                                //)
                                .mapperBuilder()
                                .superClass(IBaqiMapper.class)
                                //.enableFileOverride()
                                .mapperAnnotation(Mapper.class)
                                .serviceBuilder()
                                .superServiceClass(IBaqiService.class)
                                .superServiceImplClass(BaqiServiceImpl.class)

                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    private static void generateViewEntity(String url, String outDir, String dbUser, String dbPassword, String parentPackage) {

        FastAutoGenerator.create(url, dbUser, dbPassword)
                .globalConfig(builder -> builder
                        .author("Wcke")
                        .outputDir(outDir)
                        .commentDate("yyyy-MM-dd")
                        .disableOpenDir()
                )
                .packageConfig(builder -> builder
                        .parent(parentPackage)
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                )
                .strategyConfig(builder -> builder
                        .likeTable(new LikeTable("view_", SqlLike.RIGHT))
                        .entityBuilder()
                        .enableLombok()
                        .enableFileOverride()
                        .disableSerialVersionUID()
                        .formatFileName("%sEntity")
                        .mapperBuilder()
                        .mapperAnnotation(Mapper.class)
                        .serviceBuilder()

                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
        // 删除controller目录下以“View”开头的文件
        BQFileUtils.deleteFilesBy(outDir + "/com/qkplm/clinic/clinicserver/controller", "^View.*");
    }

    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        String url = "jdbc:mysql://localhost:3388/db_clinic?serverTimezone=Asia/Shanghai&allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8";
        String outDir = userDir + "/src/main/java";
        String dbUser = "root";
        String dbPassword = "123456";
        String parentPackage = "com.qkplm.clinic.clinicserver";
        generateEidBaseEntity(url, outDir, dbUser, dbPassword, parentPackage);
        generateIdBaseEntity(url, outDir, dbUser, dbPassword, parentPackage);
        generateViewEntity(url, outDir, dbUser, dbPassword, parentPackage);
    }
}