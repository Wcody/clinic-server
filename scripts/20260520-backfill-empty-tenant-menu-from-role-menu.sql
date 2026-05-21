-- Backfill tenant menu authorization for tenants that currently have no tenant-menu rows
-- but already have role-menu rows. This keeps custom partial tenant authorization intact.
INSERT INTO sys_tenant_menu (tenantId, menuId)
SELECT source.tenantId, source.menuId
FROM (
    SELECT r.tenantId, rm.menuId
    FROM tb_role r
    INNER JOIN sys_role_menu rm ON rm.roleId = r.eid
    LEFT JOIN tb_tenant t ON t.eid = r.tenantId
    LEFT JOIN sys_tenant_menu existing ON existing.tenantId = r.tenantId
    WHERE existing.id IS NULL
      AND (t.deleted = 0 OR r.tenantId = '04bda4f00fc44642f41bfafbb5c6f280')
    GROUP BY r.tenantId, rm.menuId
) source;
