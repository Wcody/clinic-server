/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月18日
*/
package com.qkplm.clinic.clinicserver.dtos;

import com.qkplm.clinic.clinicserver.entity.BqPrescriptionItemEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Wcke
 * @description <p>保存医嘱 请求/响应 DTO</p>
 * @datetime 2026-4-18
 */
@Getter
@Setter
public class BqSaveMedicalOrderDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // ---- 患者信息（patientId 为 null 时新增患者）----
    private Integer patientId;
    private String patientName;
    private String gender;
    private Integer firstAge;
    private Integer lastAge;
    private Integer ageType;
    private String idCard;
    private String mobile;
    private Integer province;
    private Integer city;
    private Integer district;
    private String address;
    private Boolean isAllergy;
    private String allergicHistory;

    // ---- 挂号信息（regId 为 null 时新增挂号，状态待接诊）----
    private Integer regId;
    private Boolean isFirstVisit;

    // ---- 病历ID（null 或 0 表示无关联病历）----
    private Integer recordId;

    // ---- 处方组列表（仅包含有明细行的组）----
    private List<Group> prescriptions;

    @Getter
    @Setter
    public static class Group implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /** null 表示新增处方主表 */
        private Integer prescId;
        /** 1西药 2中药 3检查 4处置 */
        private Integer prescType;
        private String groupNo;
        private BigDecimal totalPrice;
        private List<BqPrescriptionItemEntity> items;
    }

    @Getter
    @Setter
    public static class Result implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private Integer patientId;
        private Integer regId;
        /** 与请求 prescriptions 列表一一对应的处方主表 ID */
        private List<Integer> prescIds;
    }
}
