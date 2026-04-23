/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月23日
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
* @description <p>消息通知 实体类</p>
* @datetime 2026-4-23 11:50
*/
@Getter
@Setter
@TableName(value = "tb_bbs", autoResultMap = true)
public class TbBbsEntity extends BQEidBaseEntity {

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知描述
     */
    private String desc;

    /**
     * 0通知、1消息、2待办
     */
    private Integer type;

    /**
     * 0档案、1临床、2门急诊
     */
    private Integer kind;

    /**
     * 数据源表
     */
    private String source;

    /**
     * 数据eid
     */
    private String objId;

    /**
     * 所属用户eid
     */
    private String owner;

    /**
     * [B]是否已读，0未读、1已读
     */
    private Boolean hasRead;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;
}
