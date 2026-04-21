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
* @description <p>病案附件关联表 实体类</p>
* @datetime 2026-4-22 2:10
*/
@Getter
@Setter
@TableName(value = "tb_attachment", autoResultMap = true)
public class TbAttachmentEntity extends BQEidBaseEntity {

    /**
     * 病案ID
     */
    private String recordId;

    /**
     * 附件编码
     */
    private String fileCode;

    /**
     * 分类ID
     */
    private String kindId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件存储路径
     */
    private String filePath;

    /**
     * 文件类型
     */
    private String mimeType;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 文件哈希值
     */
    private String fileHash;

    /**
     * 同分类下的次序
     */
    private Integer orderValue;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * [B]是否手动上传的
     */
    private Boolean hasManual;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * [B]启用禁用, 0禁用、1启用
     */
    private Boolean status;
}
