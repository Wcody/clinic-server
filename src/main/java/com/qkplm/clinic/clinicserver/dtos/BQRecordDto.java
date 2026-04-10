/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.dtos;

import lombok.Getter;
import lombok.Setter;
import com.qkplm.clinic.clinicserver.entity.TbRecordEntity;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2025-01-05 12:26
 */
@Getter
@Setter
public class BQRecordDto extends TbRecordEntity {
    Integer ruleKind;
    Integer limitKind;
    String ruleDesc;
}
