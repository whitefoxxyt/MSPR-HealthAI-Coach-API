-- Create nutrition_entries table
CREATE TABLE nutrition_entries (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    food_name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    meal_type VARCHAR(50),
    calories NUMERIC(10, 2),
    cholesterol_mg NUMERIC(10, 2),
    protein_g NUMERIC(10, 2),
    carbs_g NUMERIC(10, 2),
    fat_g NUMERIC(10, 2),
    fiber_g NUMERIC(10, 2),
    sugars_g NUMERIC(10, 2),
    sodium_mg NUMERIC(10, 2),
    water_ml NUMERIC(10, 2),
    source VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'BRUT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_nutrition_entries_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_nutrition_entries_user_id ON nutrition_entries(user_id);
CREATE INDEX idx_nutrition_entries_created_at ON nutrition_entries(created_at);
CREATE INDEX idx_nutrition_entries_meal_type ON nutrition_entries(meal_type);
CREATE INDEX idx_nutrition_entries_status ON nutrition_entries(status);
CREATE INDEX idx_nutrition_entries_user_created ON nutrition_entries(user_id, created_at DESC);
