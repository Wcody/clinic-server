/*
 * 版权声明 Copyright (c) 2025-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.entity;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2025-01-20 21:41
 */
public class BQPDFImageLabel {
    private final String imagePath;
    private final String label;

    public BQPDFImageLabel(String imagePath, String label) {
        this.imagePath = imagePath;
        this.label = label;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getLabel() {
        return label;
    }
}
