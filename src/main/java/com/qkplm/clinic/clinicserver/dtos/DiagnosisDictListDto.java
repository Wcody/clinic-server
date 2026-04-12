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
* @description <p>疾病字典列表返回DTO</p>
* @datetime 2026-4-11
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisDictListDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 所有疾病列表
     */
    private List<DiseaseItem> diseases;

    /**
     * 疾病首字母集合(不重复,按字母排序)
     */
    private List<AlphabetItem> alphabets;

    /**
     * 所有专科列表(按sort排序)
     */
    private List<CategoryItem> categories;

    /**
     * 疾病项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiseaseItem implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        
        /**
         * 疾病ID
         */
        private Long id;
        
        /**
         * 疾病名称
         */
        private String name;
        
        /**
         * 疾病所属科室ID
         */
        private Integer category;
        
        /**
         * 疾病名称首字母
         */
        private String alphabet;
        
        /**
         * 疾病拼音(全拼)
         */
        private String pinyin;
        
        /**
         * 疾病拼音首字母串
         */
        private String pinyinInitial;
    }

    /**
     * 首字母项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlphabetItem implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        
        /**
         * 首字母
         */
        private String id;
        
        /**
         * 首字母显示名称
         */
        private String name;
    }

    /**
     * 专科项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryItem implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        
        /**
         * 科室ID
         */
        private Integer id;
        
        /**
         * 科室名称
         */
        private String name;
    }
}
