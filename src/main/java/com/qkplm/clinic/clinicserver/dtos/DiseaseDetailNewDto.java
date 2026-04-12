/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月11日
*/
package com.qkplm.clinic.clinicserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
* @author Wcke
* @description <p>疾病详情DTO(新格式)</p>
* @datetime 2026-4-11
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseDetailNewDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 疾病名称
     */
    private String name;

    /**
     * 疾病概述/小结
     */
    private String summary;

    /**
     * 详细数据
     */
    private DetailData data;

    /**
     * 详细数据结构
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailData implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 辨证治疗
         */
        private CureData cure;

        /**
         * 名医经验
         */
        private ExpData exp;
    }

    /**
     * 辨证治疗数据
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CureData implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 证型列表
         */
        private List<CureItem> data;

        /**
         * 辨证治疗总体说明
         */
        private String desc;
    }

    /**
     * 证型项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CureItem implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 证型ID
         */
        private Long id;

        /**
         * 证型名称
         */
        private String name;

        /**
         * 证候特点描述
         */
        private String spec;

        /**
         * 治法
         */
        private String solution;

        /**
         * 推荐方剂
         */
        private Prescription recommend;

        /**
         * 加减法说明列表
         */
        private List<String> additional;
    }

    /**
     * 名医经验数据
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpData implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 名医验方列表
         */
        private List<ExpItem> data;
    }

    /**
     * 名医验方项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpItem implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 验方ID
         */
        private Long id;

        /**
         * 名医姓名
         */
        private String name;

        /**
         * 名医简介
         */
        private String authorIntro;

        /**
         * 经验方名称/主治病症
         */
        private String experience;

        /**
         * 详细说明
         */
        private String explain;

        /**
         * 主要功能/主治
         */
        private String majorFunction;

        /**
         * 规格/说明
         */
        private String spec;

        /**
         * 治法
         */
        private String solution;

        /**
         * 前置说明
         */
        private String preExplain;

        /**
         * 推荐方剂
         */
        private Prescription recommend;

        /**
         * 加减法说明列表
         */
        private List<String> additional;
    }

    /**
     * 方剂信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Prescription implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 方剂名称
         */
        private String name;

        /**
         * 基础药物组成
         */
        private List<HerbItem> base;
    }

    /**
     * 药材项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HerbItem implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 药材名称
         */
        private String name;

        /**
         * 剂量数值
         */
        private Double amount;

        /**
         * 单位(如:g)
         */
        private String unit;

        /**
         * 备注(如:"10g")
         */
        private String remark;

        /**
         * 煎煮方法(如:"后下"、"冲服")
         */
        private String cook;

        /**
         * 处方类型
         */
        private Integer prescriptionType;
    }
}
