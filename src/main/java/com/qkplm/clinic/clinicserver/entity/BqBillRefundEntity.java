/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月19日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQIdBaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>退费列表(退费数据)表 实体类</p>
* @datetime 2026-4-19 19:15
*/
@Getter
@Setter
@TableName(value = "bq_bill_refund", autoResultMap = true)
public class BqBillRefundEntity extends BQIdBaseEntity {

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 退费金额
     */
    private BigDecimal returnAmount;

    /**
     * 退费时间
     */
    private LocalDateTime returnTime;

    /**
     * 退费操作人
     */
    private String returnUser;

    /**
     * [B]类型
     */
    private Boolean type;

    /**
     * 类型名称:门诊处方/住院等
     */
    private String typeString;

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
}
