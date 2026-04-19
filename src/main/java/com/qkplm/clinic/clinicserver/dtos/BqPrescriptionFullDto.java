/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月19日
*/
package com.qkplm.clinic.clinicserver.dtos;

import com.qkplm.clinic.clinicserver.entity.BqPrescriptionEntity;
import com.qkplm.clinic.clinicserver.entity.BqPrescriptionItemEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Wcke
 * @description <p>处方主表 + 明细 聚合DTO，一次性返回单张挂号的所有处方及明细</p>
 * @datetime 2026-4-19
 */
@Getter
@Setter
public class BqPrescriptionFullDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 处方主表 */
    private BqPrescriptionEntity prescription;

    /** 该处方下的所有明细（按createdTime升序） */
    private List<BqPrescriptionItemEntity> items;
}
