/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-10-16 19:54
 */
@Getter
@Setter
public class BQAttachmentOrderDto {
    private String kindId;
    private String[] list;
}
