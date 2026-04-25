/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月26日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>临床实践指导明细表 实体类</p>
* @datetime 2026-4-26 7:17
*/
@Getter
@Setter
@TableName(value = "bq_clinical_practice_detail", autoResultMap = true)
public class BqClinicalPracticeDetailEntity extends BQIdBaseEntity {

    /**
     * 关联的主题ID
     */
    private Integer bpId;

    /**
     * 诊断名称
     */
    private String dxxName;

    /**
     * 分类
     */
    private String category;

    /**
     * [B]是否常见
     */
    private Boolean common;

    /**
     * [B]是否红旗标志
     */
    private Boolean redFlag;

    /**
     * 病史内容
     */
    private String historyContent;

    /**
     * 体格检查内容
     */
    private String examContent;

    /**
     * 专著链接目标
     */
    private String monographLinkTarget;

    /**
     * 专著链接值
     */
    private String monographLinkValue;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 行乐观锁,整数,非空,默认0
     */
    private Integer version;

    /**
     * [B]逻辑删除标记,0表示false,1表示true
     */
    private Boolean deleted;

    /**
     * 删除人名称,字符串,长度256
     */
    private String deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
