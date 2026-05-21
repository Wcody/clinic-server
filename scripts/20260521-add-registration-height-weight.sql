SET @height_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'bq_registration'
      AND COLUMN_NAME = 'height'
);
SET @height_sql := IF(
    @height_exists = 0,
    'ALTER TABLE bq_registration ADD COLUMN height DECIMAL(8, 2) NULL COMMENT ''挂号时患者身高，单位cm''',
    'SELECT 1'
);
PREPARE height_stmt FROM @height_sql;
EXECUTE height_stmt;
DEALLOCATE PREPARE height_stmt;

SET @weight_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'bq_registration'
      AND COLUMN_NAME = 'weight'
);
SET @weight_sql := IF(
    @weight_exists = 0,
    'ALTER TABLE bq_registration ADD COLUMN weight DECIMAL(8, 2) NULL COMMENT ''挂号时患者体重，单位kg''',
    'SELECT 1'
);
PREPARE weight_stmt FROM @weight_sql;
EXECUTE weight_stmt;
DEALLOCATE PREPARE weight_stmt;
