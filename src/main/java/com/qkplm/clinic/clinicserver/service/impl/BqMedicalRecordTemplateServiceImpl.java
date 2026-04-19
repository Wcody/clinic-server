/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.BqMedicalRecordTemplateEntity;
import com.qkplm.clinic.clinicserver.mapper.BqMedicalRecordTemplateMapper;
import com.qkplm.clinic.clinicserver.service.IBqMedicalRecordTemplateService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author Wcke
* @description <p>病历模板表 服务接口类</p>
* @datetime 2026-4-14 12:56
*/
@Service
public class BqMedicalRecordTemplateServiceImpl extends BaqiServiceImpl<BqMedicalRecordTemplateMapper, BqMedicalRecordTemplateEntity> implements IBqMedicalRecordTemplateService {

    @Override
    public List<Map<String, Object>> getTreeData() {
        // 获取所有数据
        List<BqMedicalRecordTemplateEntity> allList = this.list();
        
        System.out.println("=== 病历模板树形数据查询 ===");
        System.out.println("查询到的记录数: " + (allList == null ? 0 : allList.size()));
        
        if (allList == null || allList.isEmpty()) {
            System.out.println("警告: 数据库中没有病历模板数据");
            return new ArrayList<>();
        }
        
        // 打印前几条数据用于调试
        allList.stream().limit(3).forEach(entity -> {
            System.out.println("记录ID: " + entity.getId() + ", 名称: " + entity.getName());
        });
        
        // 转换为Map结构，只保留id, name, parentId
        List<Map<String, Object>> nodeList = allList.stream().map(entity -> {
            Map<String, Object> node = new HashMap<>();
            node.put("id", entity.getId());
            node.put("name", entity.getName());
            node.put("parentId", entity.getParentId());
            return node;
        }).collect(Collectors.toList());
        
        System.out.println("转换后的节点数: " + nodeList.size());
        
        // 构建树形结构
        List<Map<String, Object>> treeData = buildTree(nodeList, null);
        
        System.out.println("构建的树形根节点数: " + treeData.size());
        System.out.println("===============================");
        
        return treeData;
    }
    
    /**
     * 递归构建树形结构
     * @param nodeList 所有节点列表
     * @param parentId 父节点ID
     * @return 树形结构列表
     */
    private List<Map<String, Object>> buildTree(
            List<Map<String, Object>> nodeList, 
            Integer parentId) {
        List<Map<String, Object>> treeList = new ArrayList<>();
        
        if (nodeList == null || nodeList.isEmpty()) {
            return treeList;
        }
        
        for (Map<String, Object> node : nodeList) {
            Integer nodeParentId = (Integer) node.get("parentId");
            
            // 判断是否为当前层级的节点（处理null值和0值）
            boolean isMatch;
            if (parentId == null) {
                // 查找根节点：parentId为null或0的都视为根节点
                isMatch = (nodeParentId == null || nodeParentId == 0);
            } else {
                // 查找子节点：精确匹配parentId
                isMatch = (parentId.equals(nodeParentId));
            }
            
            if (isMatch) {
                // 递归查找子节点
                Integer nodeId = (Integer) node.get("id");
                List<Map<String, Object>> children = buildTree(nodeList, nodeId);
                
                // 如果有子节点，添加children字段
                if (!children.isEmpty()) {
                    node.put("children", children);
                }
                
                treeList.add(node);
            }
        }
        
        return treeList;
    }
}
