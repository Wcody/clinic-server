/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月11日
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
* @description <p>采集器 实体类</p>
* @datetime 2026-4-11 9:28
*/
@Getter
@Setter
@TableName(value = "tb_collector", autoResultMap = true)
public class TbCollectorEntity extends BQEidBaseEntity {

    /**
     * 采集器名称
     */
    private String collectorName;

    /**
     * 电脑名称
     */
    private String computerName;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 采集器IP
     */
    private String remoteIp;

    /**
     * 采集器端口
     */
    private Integer remotePort;

    /**
     * 合计提交文档数量
     */
    private Integer processedDocs;

    /**
     * 成功入库文档数量
     */
    private Integer successDocs;

    /**
     * 最后一次执行时间
     */
    private LocalDateTime lastTime;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * [B]启用禁用, 0禁用、1启用
     */
    private Boolean status;
}
