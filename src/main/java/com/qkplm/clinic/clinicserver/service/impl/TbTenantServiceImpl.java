/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qkplm.clinic.clinicserver.entity.BqAdditionalFeeEntity;
import com.qkplm.clinic.clinicserver.entity.BqClinicDepartmentEntity;
import com.qkplm.clinic.clinicserver.entity.BqDiagnosisDictEntity;
import com.qkplm.clinic.clinicserver.entity.BqDrugEntity;
import com.qkplm.clinic.clinicserver.entity.BqEnumItemEntity;
import com.qkplm.clinic.clinicserver.entity.BqExamineItemEntity;
import com.qkplm.clinic.clinicserver.entity.BqMedicalDictionaryEntity;
import com.qkplm.clinic.clinicserver.entity.BqRegistrationFeeEntity;
import com.qkplm.clinic.clinicserver.entity.BqTreatmentItemEntity;
import com.qkplm.clinic.clinicserver.entity.TbDeptEntity;
import com.qkplm.clinic.clinicserver.entity.TbRoleEntity;
import com.qkplm.clinic.clinicserver.entity.TbTenantEntity;
import com.qkplm.clinic.clinicserver.entity.TbUserEntity;
import com.qkplm.clinic.clinicserver.mapper.TbTenantMapper;
import com.qkplm.clinic.clinicserver.service.IBqAdditionalFeeService;
import com.qkplm.clinic.clinicserver.service.IBqClinicDepartmentService;
import com.qkplm.clinic.clinicserver.service.IBqDiagnosisDictService;
import com.qkplm.clinic.clinicserver.service.IBqDrugService;
import com.qkplm.clinic.clinicserver.service.IBqEnumItemService;
import com.qkplm.clinic.clinicserver.service.IBqExamineItemService;
import com.qkplm.clinic.clinicserver.service.IBqMedicalDictionaryService;
import com.qkplm.clinic.clinicserver.service.IBqRegistrationFeeService;
import com.qkplm.clinic.clinicserver.service.IBqTreatmentItemService;
import com.qkplm.clinic.clinicserver.service.ITbDeptService;
import com.qkplm.clinic.clinicserver.service.ITbRoleService;
import com.qkplm.clinic.clinicserver.service.ITbTenantService;
import com.qkplm.clinic.clinicserver.service.ITbUserService;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.mybatis.base.BaqiServiceImpl;
import com.qkplm.clinic.libcommon.mybatis.base.IBaqiService;
import com.qkplm.clinic.libcommon.utils.BQRequestContextHolderUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
* @author Wcke
* @description <p>系统租户 服务接口类</p>
* @datetime 2024-6-16 15:49
*/
@Service
public class TbTenantServiceImpl extends BaqiServiceImpl<TbTenantMapper, TbTenantEntity> implements ITbTenantService {
    private final ITbUserService userService;
    private final ITbRoleService roleService;
    private final ITbDeptService deptService;
    private final IBqMedicalDictionaryService medicalDictionaryService;
    private final IBqEnumItemService enumItemService;
    private final IBqRegistrationFeeService registrationFeeService;
    private final IBqAdditionalFeeService additionalFeeService;
    private final IBqDiagnosisDictService diagnosisDictService;
    private final IBqExamineItemService examineItemService;
    private final IBqTreatmentItemService treatmentItemService;
    private final IBqClinicDepartmentService clinicDepartmentService;
    private final IBqDrugService drugService;

    public TbTenantServiceImpl(
            ITbUserService userService,
            ITbRoleService roleService,
            ITbDeptService deptService,
            IBqMedicalDictionaryService medicalDictionaryService,
            IBqEnumItemService enumItemService,
            IBqRegistrationFeeService registrationFeeService,
            IBqAdditionalFeeService additionalFeeService,
            IBqDiagnosisDictService diagnosisDictService,
            IBqExamineItemService examineItemService,
            IBqTreatmentItemService treatmentItemService,
            IBqClinicDepartmentService clinicDepartmentService,
            IBqDrugService drugService
    ) {
        this.userService = userService;
        this.roleService = roleService;
        this.deptService = deptService;
        this.medicalDictionaryService = medicalDictionaryService;
        this.enumItemService = enumItemService;
        this.registrationFeeService = registrationFeeService;
        this.additionalFeeService = additionalFeeService;
        this.diagnosisDictService = diagnosisDictService;
        this.examineItemService = examineItemService;
        this.treatmentItemService = treatmentItemService;
        this.clinicDepartmentService = clinicDepartmentService;
        this.drugService = drugService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TbTenantEntity createTenant(TbTenantEntity tenant) {
        tenant.setEid(null);
        if (StringUtils.isBlank(tenant.getParentId())) {
            tenant.setParentId("0");
        }
        if (Objects.isNull(tenant.getStatus())) {
            tenant.setStatus(true);
        }
        if (!save(tenant)) {
            throw new BQApiException("新增失败");
        }

        String tenantId = tenant.getEid();
        copyTenantTemplateData(tenantId);
        createInitialAdmin(tenant, tenantId);
        return tenant;
    }

    @Override
    public Page<TbTenantEntity> pageWithUsage(Page<TbTenantEntity> page, Wrapper<TbTenantEntity> queryWrapper) {
        Page<TbTenantEntity> result = page(page, queryWrapper);
        fillTenantUsage(result.getRecords());
        return result;
    }

    @Override
    public Iterable<TbTenantEntity> listWithUsage(Page<TbTenantEntity> page, Wrapper<TbTenantEntity> queryWrapper) {
        List<TbTenantEntity> result = list(page, queryWrapper);
        fillTenantUsage(result);
        return result;
    }

    @Override
    public Object setStatus(String eid, Boolean status) {
        TbTenantEntity tenant = getById(eid);
        if (Objects.nonNull(tenant)) {
            tenant.setStatus(status);
            return updateById(tenant);
        }
        throw new BQApiException("目标不存在");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTenant(String tenantId) {
        assertCanDeleteTenant(tenantId);
        cleanTenantConfigData(tenantId);
        if (!removePhysicalById(tenantId)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTenantBatch(Collection<Serializable> tenantIds) {
        for (Serializable tenantId : tenantIds) {
            assertCanDeleteTenant(String.valueOf(tenantId));
        }
        for (Serializable tenantId : tenantIds) {
            cleanTenantConfigData(String.valueOf(tenantId));
        }
        if (!removePhysicalBatchByIds(tenantIds)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    @Override
    public Object saveUserIds(String tenantId, List<String> userIds) {
        if (Objects.nonNull(userIds)) {
            getBaseMapper().deleteUserIdsBy(tenantId);
            if (userIds.isEmpty()) {
                return 0;
            }
            return getBaseMapper().saveUserIdsBy(tenantId, userIds);
        }
        throw new BQApiException("无效参数");
    }

    @Override
    public List<String> getUserIdsBy(String tenantId) {
        return getBaseMapper().getUserIdsBy(tenantId);
    }

    @Override
    public List<String> getTenantIdsBy(String userId) {
        return getBaseMapper().getTenantIdsBy(userId);
    }

    @Override
    public List<TbTenantEntity> getTenantsBy(String userId) {
        return getBaseMapper().getTenantsBy(userId);
    }

    @Override
    public Object saveMenuIds(String tenantId, List<String> menuIds) {
        if (Objects.nonNull(menuIds)) {
            getBaseMapper().deleteMenuIdsBy(tenantId);
            if (menuIds.isEmpty()) {
                return 0;
            }
            return getBaseMapper().saveMenuIdsBy(tenantId, menuIds);
        }
        throw new BQApiException("无效参数");
    }

    @Override
    public List<String> getMenuIdsBy(String tenantId) {
        return getBaseMapper().getMenuIdsBy(tenantId);
    }

    @Override
    public TbTenantEntity getByTenantId(String tenantId) {
        return getBaseMapper().selectByTenantId(tenantId);
    }

    @Override
    public boolean userIsTenantAdmin(String userId, String tenantId) {
        return getBaseMapper().userIsTenantAdmin(userId, tenantId);
    }

    private void fillTenantUsage(List<TbTenantEntity> tenants) {
        for (TbTenantEntity tenant : tenants) {
            tenant.setCurrentUserCount(getBaseMapper().countUsersByTenantId(tenant.getEid()));
            tenant.setAdminCount(getBaseMapper().countAdminsByTenantId(tenant.getEid()));
            tenant.setMenuCount(getBaseMapper().countMenusByTenantId(tenant.getEid()));
        }
    }

    private void assertCanDeleteTenant(String tenantId) {
        if (StringUtils.equals(tenantId, BQRequestContextHolderUtils.PLATFORM_TENANT_ID)) {
            throw new BQApiException("平台租户不能删除");
        }
        int adminCount = getBaseMapper().countAdminsByTenantId(tenantId);
        if (adminCount > 0) {
            throw new BQApiException("诊所存在管理员绑定，请先解绑管理员后再删除");
        }
        int userCount = getBaseMapper().countUsersByTenantId(tenantId);
        if (userCount > 0) {
            throw new BQApiException("诊所存在用户数据，请先迁移或清理用户后再删除");
        }
        int businessDataCount = getBaseMapper().countBusinessDataByTenantId(tenantId);
        if (businessDataCount > 0) {
            throw new BQApiException("诊所存在患者、挂号、病历或处方数据，不能直接删除");
        }
    }

    private void cleanTenantConfigData(String tenantId) {
        List<String> roleIds = getBaseMapper().getRoleIdsByTenantId(tenantId);
        if (!roleIds.isEmpty()) {
            getBaseMapper().deleteRoleMenusByRoleIds(roleIds);
        }
        getBaseMapper().deleteMenuIdsBy(tenantId);
        getBaseMapper().deleteUserIdsBy(tenantId);
        getBaseMapper().deleteRolesByTenantId(tenantId);
        getBaseMapper().deleteDeptsByTenantId(tenantId);
        getBaseMapper().deleteMedicalDictionaryByTenantId(tenantId);
        getBaseMapper().deleteEnumItemsByTenantId(tenantId);
        getBaseMapper().deleteRegistrationFeesByTenantId(tenantId);
        getBaseMapper().deleteAdditionalFeesByTenantId(tenantId);
        getBaseMapper().deleteDiagnosisDictsByTenantId(tenantId);
        getBaseMapper().deleteExamineItemsByTenantId(tenantId);
        getBaseMapper().deleteTreatmentItemsByTenantId(tenantId);
        getBaseMapper().deleteClinicDepartmentsByTenantId(tenantId);
        getBaseMapper().deleteDrugsByTenantId(tenantId);
    }

    private void copyTenantTemplateData(String targetTenantId) {
        copyTenantMenuTemplates(targetTenantId);
        copyDeptTemplates(targetTenantId);
        copyRoleTemplates(targetTenantId);
        ensureTenantMenus(targetTenantId);
        copyTenantInitData(medicalDictionaryService, targetTenantId);
        copyTenantInitData(enumItemService, targetTenantId);
        copyTenantInitData(registrationFeeService, targetTenantId);
        copyTenantInitData(additionalFeeService, targetTenantId);
        copyTenantInitData(diagnosisDictService, targetTenantId);
        copyTenantInitData(examineItemService, targetTenantId);
        copyTenantInitData(treatmentItemService, targetTenantId);
        copyTenantInitData(clinicDepartmentService, targetTenantId);
        copyDrugTemplates(targetTenantId);
    }

    private void copyTenantMenuTemplates(String targetTenantId) {
        getBaseMapper().copyMenuIdsBy(BQRequestContextHolderUtils.PLATFORM_TENANT_ID, targetTenantId);
    }

    private void ensureTenantMenus(String targetTenantId) {
        List<String> menuIds = getBaseMapper().getMenuIdsBy(targetTenantId);
        if (menuIds.isEmpty()) {
            getBaseMapper().copyMenuIdsByTenantRoleIds(targetTenantId);
        }
    }

    private void createInitialAdmin(TbTenantEntity tenant, String tenantId) {
        if (StringUtils.isBlank(tenant.getAdminAccount())) {
            return;
        }
        if (StringUtils.isBlank(tenant.getAdminPassword())) {
            throw new BQApiException("管理员初始密码不能为空");
        }
        if (userService.getByAccountAndTenantId(tenant.getAdminAccount(), tenantId).isPresent()) {
            throw new BQApiException("管理员账号已存在");
        }

        TbUserEntity admin = new TbUserEntity();
        admin.setTenantId(tenantId);
        admin.setParentId("0");
        admin.setAccount(tenant.getAdminAccount());
        admin.setPassword(tenant.getAdminPassword());
        admin.setName(defaultIfBlank(tenant.getAdminName(), tenant.getPrincipal(), tenant.getAdminAccount()));
        admin.setNickname(admin.getName());
        admin.setPhone(defaultIfBlank(tenant.getAdminPhone(), tenant.getPhone(), ""));
        admin.setEmail(defaultIfBlank(tenant.getAdminEmail(), tenant.getEmail(), ""));
        admin.setSex((byte) 0);
        admin.setStatus(true);
        admin.setTenantInitData(false);

        Boolean saved = withTenant(tenantId, () -> userService.saveWithPassword(admin));
        if (!Boolean.TRUE.equals(saved)) {
            throw new BQApiException("初始化管理员账号失败");
        }
        getBaseMapper().saveUserIdsBy(tenantId, List.of(admin.getEid()));
    }

    private void copyDeptTemplates(String targetTenantId) {
        List<TbDeptEntity> sources = withTenant(
                BQRequestContextHolderUtils.PLATFORM_TENANT_ID,
                () -> deptService.list(new QueryWrapper<TbDeptEntity>().eq("tenantInitData", true))
        );
        if (sources.isEmpty()) {
            return;
        }

        List<TbDeptEntity> targets = new ArrayList<>();
        for (TbDeptEntity source : sources) {
            targets.add(cloneTenantEntity(source, targetTenantId));
        }

        withTenant(targetTenantId, () -> deptService.saveBatch(targets));

        Map<String, String> oldToNewIds = new HashMap<>();
        for (int i = 0; i < sources.size(); i++) {
            oldToNewIds.put(sources.get(i).getEid(), targets.get(i).getEid());
        }

        boolean parentChanged = false;
        for (int i = 0; i < sources.size(); i++) {
            String sourceParentId = sources.get(i).getParentId();
            if (oldToNewIds.containsKey(sourceParentId)) {
                targets.get(i).setParentId(oldToNewIds.get(sourceParentId));
                parentChanged = true;
            }
        }
        if (parentChanged) {
            withTenant(targetTenantId, () -> deptService.updateBatchById(targets));
        }
    }

    private void copyRoleTemplates(String targetTenantId) {
        List<TbRoleEntity> sources = withTenant(
                BQRequestContextHolderUtils.PLATFORM_TENANT_ID,
                () -> roleService.list(new QueryWrapper<TbRoleEntity>().eq("tenantInitData", true))
        );
        if (sources.isEmpty()) {
            return;
        }

        List<TbRoleEntity> targets = new ArrayList<>();
        for (TbRoleEntity source : sources) {
            targets.add(cloneTenantEntity(source, targetTenantId));
        }

        withTenant(targetTenantId, () -> roleService.saveBatch(targets));
        for (int i = 0; i < sources.size(); i++) {
            getBaseMapper().copyRoleMenuIdsBy(sources.get(i).getEid(), targets.get(i).getEid());
        }
    }

    private <T> void copyTenantInitData(IBaqiService<T> service, String targetTenantId) {
        List<T> sources = withTenant(
                BQRequestContextHolderUtils.PLATFORM_TENANT_ID,
                () -> service.list(new QueryWrapper<T>().eq("tenantInitData", true))
        );
        if (sources.isEmpty()) {
            return;
        }

        List<T> targets = sources.stream()
                .map(source -> cloneTenantEntity(source, targetTenantId))
                .toList();
        withTenant(targetTenantId, () -> service.saveBatch(targets));
    }

    private void copyDrugTemplates(String targetTenantId) {
        List<BqDrugEntity> sources = withTenant(
                BQRequestContextHolderUtils.PLATFORM_TENANT_ID,
                () -> drugService.list(new QueryWrapper<BqDrugEntity>().eq("tenantInitData", true))
        );
        if (sources.isEmpty()) {
            return;
        }

        Map<Integer, String> sourceUnitNameById = loadUnitNameById(BQRequestContextHolderUtils.PLATFORM_TENANT_ID);
        Map<String, Integer> targetUnitIdByName = loadUnitIdByName(targetTenantId);
        List<BqDrugEntity> targets = sources.stream()
                .map(source -> {
                    BqDrugEntity target = cloneTenantEntity(source, targetTenantId);
                    normalizeDrugUnits(target, sourceUnitNameById, targetUnitIdByName);
                    return target;
                })
                .toList();
        withTenant(targetTenantId, () -> drugService.saveBatch(targets));
    }

    private Map<Integer, String> loadUnitNameById(String tenantId) {
        return withTenant(
                tenantId,
                () -> medicalDictionaryService.list(new QueryWrapper<BqMedicalDictionaryEntity>().eq("dictType", 3))
        ).stream()
                .filter(item -> Objects.nonNull(item.getId()) && StringUtils.isNotBlank(item.getName()))
                .collect(Collectors.toMap(BqMedicalDictionaryEntity::getId, BqMedicalDictionaryEntity::getName, (a, b) -> a));
    }

    private Map<String, Integer> loadUnitIdByName(String tenantId) {
        return withTenant(
                tenantId,
                () -> medicalDictionaryService.list(new QueryWrapper<BqMedicalDictionaryEntity>().eq("dictType", 3))
        ).stream()
                .filter(item -> Objects.nonNull(item.getId()) && StringUtils.isNotBlank(item.getName()))
                .collect(Collectors.toMap(BqMedicalDictionaryEntity::getName, BqMedicalDictionaryEntity::getId, (a, b) -> a));
    }

    private void normalizeDrugUnits(BqDrugEntity drug,
                                    Map<Integer, String> sourceUnitNameById,
                                    Map<String, Integer> targetUnitIdByName) {
        drug.setWholesaleUnit(resolveUnitName(drug.getWholesaleUnit(), sourceUnitNameById));
        drug.setPrescriptionUnit(resolveUnitName(drug.getPrescriptionUnit(), sourceUnitNameById));
        drug.setUnitId(resolveTargetUnitId(drug.getUnitId(), sourceUnitNameById, targetUnitIdByName));
        drug.setInitialStockUnitId(resolveTargetUnitIdText(drug.getInitialStockUnitId(), sourceUnitNameById, targetUnitIdByName));
    }

    private String resolveUnitName(String value, Map<Integer, String> sourceUnitNameById) {
        Integer unitId = parseInteger(value);
        if (Objects.isNull(unitId)) {
            return value;
        }
        return sourceUnitNameById.getOrDefault(unitId, value);
    }

    private Integer resolveTargetUnitId(Integer sourceUnitId,
                                        Map<Integer, String> sourceUnitNameById,
                                        Map<String, Integer> targetUnitIdByName) {
        if (Objects.isNull(sourceUnitId)) {
            return null;
        }
        String unitName = sourceUnitNameById.get(sourceUnitId);
        if (StringUtils.isBlank(unitName)) {
            return sourceUnitId;
        }
        return targetUnitIdByName.getOrDefault(unitName, sourceUnitId);
    }

    private String resolveTargetUnitIdText(String value,
                                           Map<Integer, String> sourceUnitNameById,
                                           Map<String, Integer> targetUnitIdByName) {
        Integer sourceUnitId = parseInteger(value);
        if (Objects.isNull(sourceUnitId)) {
            return value;
        }
        Integer targetUnitId = resolveTargetUnitId(sourceUnitId, sourceUnitNameById, targetUnitIdByName);
        return Objects.isNull(targetUnitId) ? value : String.valueOf(targetUnitId);
    }

    private Integer parseInteger(String value) {
        if (StringUtils.isBlank(value) || !value.matches("\\d+")) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private <T> T cloneTenantEntity(T source, String targetTenantId) {
        try {
            @SuppressWarnings("unchecked")
            T target = (T) source.getClass().getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            resetTenantEntity(target, targetTenantId);
            return target;
        } catch (Exception e) {
            throw new BQApiException("复制租户初始化数据失败：" + e.getMessage());
        }
    }

    private void resetTenantEntity(Object target, String tenantId) {
        setFieldIfPresent(target, "id", null);
        setFieldIfPresent(target, "eid", null);
        setFieldIfPresent(target, "tenantId", tenantId);
        setFieldIfPresent(target, "createdTime", null);
        setFieldIfPresent(target, "updatedTime", null);
        setFieldIfPresent(target, "createdBy", null);
        setFieldIfPresent(target, "updatedBy", null);
        setFieldIfPresent(target, "createTime", null);
        setFieldIfPresent(target, "updateTime", null);
        setFieldIfPresent(target, "deletedTime", null);
        setFieldIfPresent(target, "deletedBy", null);
        setFieldIfPresent(target, "tenantInitData", false);
        setTypedFieldIfPresent(target, "version", 0);
        setTypedFieldIfPresent(target, "deleted", false);
    }

    private void setFieldIfPresent(Object target, String fieldName, Object value) {
        Field field = findField(target.getClass(), fieldName);
        if (Objects.isNull(field)) {
            return;
        }
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new BQApiException("设置初始化字段失败：" + fieldName);
        }
    }

    private void setTypedFieldIfPresent(Object target, String fieldName, Object value) {
        Field field = findField(target.getClass(), fieldName);
        if (Objects.isNull(field)) {
            return;
        }
        try {
            field.setAccessible(true);
            if (field.getType().equals(Byte.class)) {
                field.set(target, (byte) 0);
            } else if (field.getType().equals(Integer.class)) {
                field.set(target, 0);
            } else if (field.getType().equals(Boolean.class)) {
                field.set(target, Boolean.FALSE);
            } else {
                field.set(target, value);
            }
        } catch (IllegalAccessException e) {
            throw new BQApiException("设置初始化字段失败：" + fieldName);
        }
    }

    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (Objects.nonNull(current) && !current.equals(Object.class)) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    private <T> T withTenant(String tenantId, Supplier<T> supplier) {
        BQRequestContextHolderUtils.setSourceTenantId(tenantId);
        try {
            return supplier.get();
        } finally {
            BQRequestContextHolderUtils.removeSourceTenantId();
        }
    }

    private String defaultIfBlank(String value, String fallback, String defaultValue) {
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        if (StringUtils.isNotBlank(fallback)) {
            return fallback;
        }
        return defaultValue;
    }
}
