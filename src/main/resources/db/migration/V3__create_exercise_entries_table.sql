-- Create exercise_entries table
CREATE TABLE exercise_entries (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    workout_type VARCHAR(100),
    duration_min NUMERIC(10, 2),
    calories_burned NUMERIC(10, 2),
    steps INTEGER,
    heart_rate_avg INTEGER,
    heart_rate_max INTEGER,
    source VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'BRUT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_exercise_entries_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_exercise_entries_user_id ON exercise_entries(user_id);
CREATE INDEX idx_exercise_entries_created_at ON exercise_entries(created_at);
CREATE INDEX idx_exercise_entries_status ON exercise_entries(status);
CREATE INDEX idx_exercise_entries_user_created ON exercise_entries(user_id, created_at DESC);
