/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月22日
*/
package com.qkplm.clinic.clinicserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qkplm.clinic.libcommon.entity.BQEidBaseEntity;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* @author Wcke
* @description <p>水印设置表 实体类</p>
* @datetime 2026-4-22 2:10
*/
@Getter
@Setter
@TableName(value = "tb_watermark", autoResultMap = true)
public class TbWatermarkEntity extends BQEidBaseEntity {

    /**
     * [B]启用禁用浏览文本水印
     */
    private Boolean textView;

    /**
     * [B]启用禁用打印文本水印
     */
    private Boolean textPrint;

    /**
     * [B]启用禁用下载文本水印
     */
    private Boolean textDown;

    /**
     * [B]启用禁用浏览图片水印
     */
    private Boolean imageView;

    /**
     * [B]启用禁用打印图片水印
     */
    private Boolean imagePrint;

    /**
     * [B]启用禁用下载图片水印
     */
    private Boolean imageDown;

    /**
     * [O]水印设置值
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ObjectNode paramValue;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * 逻辑删除
     */
    private Integer delete;

    /**
     * [B]启用禁用
     */
    private Boolean status;
}
