-- Add SaaS tenant authorization type.
-- Tenant authorization belongs to tb_tenant, while tb_customer is for independent deployment authorization.

SET @schema_name := DATABASE();

SET @add_tenant_auth_type_sql := (
    SELECT IF(
        NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = @schema_name
              AND table_name = 'tb_tenant'
              AND column_name = 'authType'
        ),
        'ALTER TABLE tb_tenant ADD COLUMN authType INT NOT NULL DEFAULT 0 COMMENT ''授权类型，0临时授权，1正式授权，2永久授权'' AFTER status',
        'SELECT 1'
    )
);
PREPARE stmt FROM @add_tenant_auth_type_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE OR REPLACE VIEW sel_tenant AS
SELECT
  uid,
  eid,
  parentId,
  parentName,
  name,
  principal,
  phone,
  email,
  address,
  tenantKey,
  status,
  authType,
  maxUserCount,
  deviceCode,
  expireDate,
  remark,
  lastUpdatedTime,
  version,
  deleted,
  deletedTime,
  createdBy,
  createdTime,
  updatedBy,
  updatedTime,
  tenantId
FROM tb_tenant;
