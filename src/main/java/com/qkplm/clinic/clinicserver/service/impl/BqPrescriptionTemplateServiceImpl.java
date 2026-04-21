/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月14日
*/

package com.qkplm.clinic.clinicserver.service.impl;

import com.qkplm.clinic.clinicserver.entity.BqPrescriptionTemplateEntity;
import com.qkplm.clinic.clinicserver.mapper.BqPrescriptionTemplateMapper;
import com.qkplm.clinic.clinicserver.service.IBqPrescriptionTemplateService;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Wcke
* @description <p>处方模板主表 服务接口类</p>
* @datetime 2026-4-14 12:56
*/
@Service
public class BqPrescriptionTemplateServiceImpl extends BaqiServiceImpl<BqPrescriptionTemplateMapper, BqPrescriptionTemplateEntity> implements IBqPrescriptionTemplateService {

    @Override
    public List<Map<String, Object>> getTreeData() {
        // 获取所有数据
        List<BqPrescriptionTemplateEntity> allList = this.list();

        System.out.println("=== 处方模板树形数据查询 ===");
        System.out.println("查询到的记录数: " + (allList == null ? 0 : allList.size()));

        if (allList == null || allList.isEmpty()) {
            System.out.println("警告: 数据库中没有处方模板数据");
            return new ArrayList<>();
        }

        // 打印前几条数据用于调试
        allList.stream().limit(3).forEach(entity -> {
            System.out.println("记录ID: " + entity.getId() + ", 名称: " + entity.getName() +
                             ", parentId: " + entity.getParentId() +
                             ", hasCategory: " + entity.getHasCategory());
        });

        // 转换为Map结构，只保留id, name, parentId, hasCategory, prescriptionType
        List<Map<String, Object>> nodeList = allList.stream().map(entity -> {
            Map<String, Object> node = new HashMap<>();
            node.put("id", entity.getId());
            node.put("name", entity.getName());
            node.put("parentId", entity.getParentId());
            node.put("hasCategory", entity.getHasCategory() != null ? entity.getHasCategory() : false);
            node.put("prescriptionType", entity.getPrescriptionType());
            return node;
        }).collect(Collectors.toList());

        System.out.println("转换后的节点数: " + nodeList.size());

        // 构建树形结构（处理 parentId 找不到对应节点的情况）
        List<Map<String, Object>> treeData = buildTree(nodeList);

        System.out.println("构建的树形根节点数: " + treeData.size());
        System.out.println("===============================");

        return treeData;
    }

    /**
     * 构建树形结构 - 将 parentId 没有对应父节点的节点拼接到根节点
     * @param nodeList 所有节点列表
     * @return 树形结构列表
     */
    private List<Map<String, Object>> buildTree(List<Map<String, Object>> nodeList) {
        List<Map<String, Object>> treeList = new ArrayList<>();

        if (nodeList == null || nodeList.isEmpty()) {
            return treeList;
        }

        // 收集所有有效的节点ID，用于判断parentId是否有效
        Set<Integer> allNodeIds = new HashSet<>();
        for (Map<String, Object> node : nodeList) {
            Integer id = (Integer) node.get("id");
            if (id != null) {
                allNodeIds.add(id);
            }
        }

        // 遍历所有节点，找出根节点并构建完整的树
        for (Map<String, Object> node : nodeList) {
            Integer nodeId = (Integer) node.get("id");
            Integer nodeParentId = (Integer) node.get("parentId");

            // 作为根节点的条件：
            // 1. parentId 为 null 或 0
            // 2. parentId 等于自己的 id（自引用）
            // 3. parentId 在所有节点中找不到对应节点
            boolean isRoot = nodeParentId == null || nodeParentId == 0
                    || nodeParentId.equals(nodeId)
                    || !allNodeIds.contains(nodeParentId);

            if (isRoot) {
                // 避免重复：将已确定为根节点的节点从待处理集合中移除
                // 递归构建子树
                buildChildren(nodeList, node, allNodeIds);
                treeList.add(node);
            }
        }

        return treeList;
    }

    /**
     * 递归构建子节点
     * @param nodeList 所有节点列表
     * @param parentNode 父节点
     * @param allNodeIds 所有有效节点ID集合
     */
    private void buildChildren(List<Map<String, Object>> nodeList, Map<String, Object> parentNode, Set<Integer> allNodeIds) {
        Integer parentId = (Integer) parentNode.get("id");
        List<Map<String, Object>> childrenList = new ArrayList<>();

        for (Map<String, Object> node : nodeList) {
            Integer nodeParentId = (Integer) node.get("parentId");
            Integer nodeId = (Integer) node.get("id");

            // 跳过自身
            if (nodeId.equals(parentId)) {
                continue;
            }

            // 查找当前父节点的子节点：parentId 匹配，且不是自引用
            if (nodeParentId != null && nodeParentId.equals(parentId) && !nodeParentId.equals(nodeId)) {
                // 递归构建子节点的children
                buildChildren(nodeList, node, allNodeIds);
                childrenList.add(node);
            }
        }

        if (!childrenList.isEmpty()) {
            parentNode.put("children", childrenList);
        }
    }
}
