-- Create biometric_entries table
CREATE TABLE biometric_entries (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    weight_kg NUMERIC(5, 2),
    height_cm NUMERIC(5, 2),
    bmi NUMERIC(5, 2),
    fat_percentage NUMERIC(5, 2),
    heart_rate_rest INTEGER,
    heart_rate_avg INTEGER,
    heart_rate_max INTEGER,
    blood_pressure VARCHAR(20),
    source VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'BRUT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_biometric_entries_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_biometric_entries_user_id ON biometric_entries(user_id);
CREATE INDEX idx_biometric_entries_created_at ON biometric_entries(created_at);
CREATE INDEX idx_biometric_entries_status ON biometric_entries(status);
CREATE INDEX idx_biometric_entries_user_created ON biometric_entries(user_id, created_at DESC);
