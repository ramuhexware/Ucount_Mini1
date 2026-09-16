-- =========================================================
-- Stage 2 Database Schema (DB2: ucount_stage2_db)
-- Extended Profile, Rules Engine Entitlements & ACR Archiving
-- =========================================================

CREATE TABLE IF NOT EXISTS ucount_stage2_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    profile_id VARCHAR(50) UNIQUE NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    user_type VARCHAR(50) NOT NULL, -- HOUSE_SELLER, HOUSE_BUYER, INSURANCE_AGENT, MORTGAGE_SERVICER
    financial_history_rating VARCHAR(30) NOT NULL,
    annual_revenue DECIMAL(15, 2),
    years_in_business INT NOT NULL,
    compliance_passed BOOLEAN DEFAULT TRUE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ucount_user_access_rights (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    profile_id VARCHAR(50) NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    service_name VARCHAR(100) NOT NULL, -- e.g. LOAN_ORIGINATION_PORTAL, SECONDARY_MARKET_ACCESS, TITLE_PORTAL
    permission_level VARCHAR(30) NOT NULL, -- READ_ONLY, FULL_ACCESS, RESTRICTED
    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for ControlM ACR (Annual Certificate Repair) Job Archiving
CREATE TABLE IF NOT EXISTS ucount_archived_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_profile_id VARCHAR(50) NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    user_type VARCHAR(50) NOT NULL,
    archive_reason VARCHAR(100) NOT NULL,
    archived_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payload_json TEXT
);

CREATE INDEX idx_stage2_profile_id ON ucount_stage2_profiles(profile_id);
CREATE INDEX idx_stage2_user_type ON ucount_stage2_profiles(user_type);
CREATE INDEX idx_access_user_id ON ucount_user_access_rights(user_id);
