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
* @description <p>诊断性检查表 实体类</p>
* @datetime 2026-4-26 7:17
*/
@Getter
@Setter
@TableName(value = "bq_diagnostic_test", autoResultMap = true)
public class BqDiagnosticTestEntity extends BQIdBaseEntity {

    /**
     * 关联的明细ID
     */
    private String detailId;

    /**
     * 检查名称
     */
    private String testName;

    /**
     * [B]是否首要检查
     */
    private Boolean firstFlag;

    /**
     * 检查结果
     */
    private String resultContent;

    /**
     * 备注说明
     */
    private String commentsContent;

    /**
     * 检查类型(initialTest/emergingTest/subsequentTest)
     */
    private String testType;

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
