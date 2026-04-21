/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月22日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>病案附件批注信息表 实体类</p>
* @datetime 2026-4-22 2:10
*/
@Getter
@Setter
@TableName(value = "tb_annotation", autoResultMap = true)
public class TbAnnotationEntity extends BQEidBaseEntity {

    /**
     * 病案ID
     */
    private String recordId;

    /**
     * 附件ID
     */
    private String attachmentId;

    /**
     * 质控员ID
     */
    private String qualityControlId;

    /**
     * 批注分类
     */
    private String annotationKind;

    /**
     * 批注项目
     */
    private String annotationItem;

    /**
     * 批注内容
     */
    private String annotationContent;

    /**
     * 扣分值
     */
    private Integer deductionPoints;

    /**
     * x坐标
     */
    private Integer x;

    /**
     * y坐标
     */
    private Integer y;

    /**
     * 宽度
     */
    private Integer w;

    /**
     * 高度
     */
    private Integer h;

    /**
     * 修复者ID
     */
    private String fixer;

    /**
     * 修复时间
     */
    private LocalDateTime fixTime;

    /**
     * [B]批注状态, 0无效、1有效
     */
    private Boolean status;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
