-- Make role code/name unique inside one tenant instead of globally.
-- This is required because platform template roles are copied into SaaS tenants.

SET @schema_name := DATABASE();

SET @drop_code_unique_sql := (
    SELECT IF(
        EXISTS (
            SELECT 1
            FROM information_schema.statistics
            WHERE table_schema = @schema_name
              AND table_name = 'tb_role'
              AND index_name = 'code_UNIQUE'
        ),
        'ALTER TABLE tb_role DROP INDEX code_UNIQUE',
        'SELECT 1'
    )
);
PREPARE stmt FROM @drop_code_unique_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @drop_name_unique_sql := (
    SELECT IF(
        EXISTS (
            SELECT 1
            FROM information_schema.statistics
            WHERE table_schema = @schema_name
              AND table_name = 'tb_role'
              AND index_name = 'name_UNIQUE'
        ),
        'ALTER TABLE tb_role DROP INDEX name_UNIQUE',
        'SELECT 1'
    )
);
PREPARE stmt FROM @drop_name_unique_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @add_tenant_code_unique_sql := (
    SELECT IF(
        NOT EXISTS (
            SELECT 1
            FROM information_schema.statistics
            WHERE table_schema = @schema_name
              AND table_name = 'tb_role'
              AND index_name = 'uk_tb_role_tenant_code'
        ),
        'ALTER TABLE tb_role ADD UNIQUE INDEX uk_tb_role_tenant_code (tenantId, code)',
        'SELECT 1'
    )
);
PREPARE stmt FROM @add_tenant_code_unique_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @add_tenant_name_unique_sql := (
    SELECT IF(
        NOT EXISTS (
            SELECT 1
            FROM information_schema.statistics
            WHERE table_schema = @schema_name
              AND table_name = 'tb_role'
              AND index_name = 'uk_tb_role_tenant_name'
        ),
        'ALTER TABLE tb_role ADD UNIQUE INDEX uk_tb_role_tenant_name (tenantId, name)',
        'SELECT 1'
    )
);
PREPARE stmt FROM @add_tenant_name_unique_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
