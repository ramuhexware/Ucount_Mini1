-- =========================================================
-- Stage 1 Database Schema (DB1: ucount_stage1_db)
-- Unified Counterparty Intake & Basic Investigation
-- =========================================================

CREATE TABLE IF NOT EXISTS ucount_stage1_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    organization_name VARCHAR(150) NOT NULL,
    tax_id VARCHAR(50) NOT NULL,
    work_email VARCHAR(100) NOT NULL,
    contact_number VARCHAR(30) NOT NULL,
    work_address VARCHAR(255) NOT NULL,
    network_domain VARCHAR(100) NOT NULL,
    approval_status VARCHAR(30) NOT NULL DEFAULT 'PENDING_APPROVAL',
    investigation_notes VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ucount_auth_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    token_value VARCHAR(512) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_stage1_user_id ON ucount_stage1_users(user_id);
CREATE INDEX idx_stage1_status ON ucount_stage1_users(approval_status);
