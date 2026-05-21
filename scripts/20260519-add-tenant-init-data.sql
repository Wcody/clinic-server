-- Add tenant initialization marker for platform tenant templates.
-- tenantInitData = true means the row can be copied from the platform tenant to a new SaaS tenant.

ALTER TABLE tb_user ADD COLUMN IF NOT EXISTS tenantInitData BOOLEAN DEFAULT TRUE;
ALTER TABLE tb_role ADD COLUMN IF NOT EXISTS tenantInitData BOOLEAN DEFAULT TRUE;
ALTER TABLE tb_dept ADD COLUMN IF NOT EXISTS tenantInitData BOOLEAN DEFAULT TRUE;

ALTER TABLE bq_medical_dictionary ADD COLUMN IF NOT EXISTS tenantInitData BOOLEAN DEFAULT TRUE;
ALTER TABLE bq_enum_item ADD COLUMN IF NOT EXISTS tenantInitData BOOLEAN DEFAULT TRUE;
ALTER TABLE bq_registration_fee ADD COLUMN IF NOT EXISTS tenantInitData BOOLEAN DEFAULT TRUE;
ALTER TABLE bq_additional_fee ADD COLUMN IF NOT EXISTS tenantInitData BOOLEAN DEFAULT TRUE;
ALTER TABLE bq_diagnosis_dict ADD COLUMN IF NOT EXISTS tenantInitData BOOLEAN DEFAULT TRUE;
ALTER TABLE bq_examine_item ADD COLUMN IF NOT EXISTS tenantInitData BOOLEAN DEFAULT TRUE;
ALTER TABLE bq_treatment_item ADD COLUMN IF NOT EXISTS tenantInitData BOOLEAN DEFAULT TRUE;
ALTER TABLE bq_clinic_department ADD COLUMN IF NOT EXISTS tenantInitData BOOLEAN DEFAULT TRUE;
ALTER TABLE bq_drug ADD COLUMN IF NOT EXISTS tenantInitData BOOLEAN DEFAULT TRUE;