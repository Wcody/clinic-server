/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月29日
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
* @description <p>检查检验项目表 实体类</p>
* @datetime 2026-4-29 17:45
*/
@Getter
@Setter
@TableName(value = "bq_examine_item", autoResultMap = true)
public class BqExamineItemEntity extends BQIdBaseEntity {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 序号
     */
    private String seq;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 销售价格
     */
    private String sellingPrice;

    /**
     * 成本价格
     */
    private String costPrice;

    /**
     * 状态:启用/禁用
     */
    private String status;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

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

    /**
     * 名称拼音首字母
     */
    private String pinyin;
    /**
     * [B]租户初始化数据
     */
    private Boolean tenantInitData;
}
