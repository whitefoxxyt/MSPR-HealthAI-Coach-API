-- Create exercises table
CREATE TABLE exercises (
    id BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    body_parts TEXT[],
    target_muscles TEXT[],
    secondary_muscles TEXT[],
    equipments TEXT[],
    instructions TEXT,
    gif_url VARCHAR(500),
    source VARCHAR(50) NOT NULL DEFAULT 'EXERCISEDB',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_exercises_external_id ON exercises(external_id);
CREATE INDEX idx_exercises_name ON exercises(name);
CREATE INDEX idx_exercises_source ON exercises(source);
