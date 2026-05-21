-- Make user account unique inside one tenant instead of globally.
-- This allows different SaaS tenants to use the same login account name.

SET @schema_name := DATABASE();

SET @drop_account_unique_sql := (
    SELECT IF(
        EXISTS (
            SELECT 1
            FROM information_schema.statistics
            WHERE table_schema = @schema_name
              AND table_name = 'tb_user'
              AND index_name = 'account_UNIQUE'
        ),
        'ALTER TABLE tb_user DROP INDEX account_UNIQUE',
        'SELECT 1'
    )
);
PREPARE stmt FROM @drop_account_unique_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @add_tenant_account_unique_sql := (
    SELECT IF(
        NOT EXISTS (
            SELECT 1
            FROM information_schema.statistics
            WHERE table_schema = @schema_name
              AND table_name = 'tb_user'
              AND index_name = 'uk_tb_user_tenant_account'
        ),
        'ALTER TABLE tb_user ADD UNIQUE INDEX uk_tb_user_tenant_account (tenantId, account)',
        'SELECT 1'
    )
);
PREPARE stmt FROM @add_tenant_account_unique_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
