/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月11日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>分类模板 实体类</p>
* @datetime 2026-4-11 9:28
*/
@Getter
@Setter
@TableName(value = "tb_kind_template", autoResultMap = true)
public class TbKindTemplateEntity extends BQEidBaseEntity {

    /**
     * 所属分类ID
     */
    private String kindId;

    /**
     * 分类模板名称
     */
    private String name;

    /**
     * [B]启用禁用状态
     */
    private Boolean status;

    /**
     * 模板图片物理路径
     */
    private String imgPath;

    /**
     * 图片宽度
     */
    private Integer imgWidth;

    /**
     * 图片高度
     */
    private Integer imgHeight;

    /**
     * 图片大小
     */
    private Long imgSize;

    /**
     * 图片的类型
     */
    private String nineType;

    /**
     * [A]识别关键字列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ArrayNode regKeys;

    /**
     * [A]数据字段列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ArrayNode dataFields;

    /**
     * 备注信息
     */
    private String remark;
}
