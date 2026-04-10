/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月10日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>收费主表 实体类</p>
* @datetime 2026-4-10 18:51
*/
@Getter
@Setter
@TableName(value = "tb_fee", autoResultMap = true)
public class TbFeeEntity extends BQEidBaseEntity {

    /**
     * 收费单号
     */
    private String feeNo;

    /**
     * 就诊记录ID
     */
    private Integer recordId;

    /**
     * 挂号ID
     */
    private Integer regId;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 处方ID串
     */
    private String prescIds;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 1微信 2支付宝 3现金
     */
    private Byte payType;

    /**
     * 1未付 2已付
     */
    private Byte payStatus;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    private LocalDateTime createTime;

    private LocalDateTime payTime;
}
