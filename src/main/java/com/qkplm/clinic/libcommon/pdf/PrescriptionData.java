/*
 * 版权声明 Copyright (c) 2026。
 * 版权所有者： [九维无纸化病案管理系统]
 */
package com.qkplm.clinic.libcommon.pdf;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 处方PDF生成用的数据对象（纯数据类，不依赖任何框架）
 * 用于本地 main 调试
 */
@Getter
@Setter
public class PrescriptionData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 挂号信息 ====================
    private String patientName;
    private String gender;
    private String age;
    private String ageType;  // 1=岁,2=月,3=天
    private Integer firstAge;
    private LocalDateTime orderTime;
    private String doctor;
    private String patientId; // 挂号表里的 patientId

    // ==================== 患者信息 ====================
    private String allergicHistory; // 过敏史

    // ==================== 病历信息 ====================
    private String diagnosis; // 诊断

    // ==================== 处方信息 ====================
    private String prescNo;
    private Integer prescId;
    private Byte prescType; // 1=西药,2=中药,3=检查,4=处置
    private BigDecimal totalPrice;
    private List<PrescriptionItem> items;

    @Getter
    @Setter
    public static class PrescriptionItem implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private Byte itemType;   // 1=西药,2=中药,3=中成药,101=检查检验,102=处置项目,103=附加费
        private String itemName;
        private String spec;
        private String singleDosage;
        private String unit;
        private String useWay;
        private String frequency;
        private Integer days;
        private BigDecimal totalNum;
        private String entrust;
        private BigDecimal totalPrice;
    }
}