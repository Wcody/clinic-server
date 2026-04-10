/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月10日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.math.BigDecimal;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>审批工作流 实体类</p>
* @datetime 2026-4-10 18:51
*/
@Getter
@Setter
@TableName(value = "tb_flow", autoResultMap = true)
public class TbFlowEntity extends BQEidBaseEntity {

    /**
     * 流程设置名称
     */
    private String name;

    /**
     * [A]设置的流程节点
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ArrayNode nodes;

    /**
     * [A]设置的连接节点
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ArrayNode edges;

    /**
     * [A]位置信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ArrayNode position;

    /**
     * [O]视窗信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ObjectNode viewport;

    /**
     * 当前缩放大小
     */
    private BigDecimal zoom;

    /**
     * [B]启用禁用状态
     */
    private Boolean status;
}
