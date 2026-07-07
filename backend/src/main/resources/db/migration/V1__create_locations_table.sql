CREATE TABLE locations (
    id BIGSERIAL PRIMARY KEY,
    image_url VARCHAR(500) NOT NULL,
    thumbnail_url VARCHAR(500),
    correct_lat DOUBLE PRECISION NOT NULL,
    correct_lng DOUBLE PRECISION NOT NULL,
    correct_year INTEGER NOT NULL,
    location_name VARCHAR(255) NOT NULL,
    fun_fact TEXT,
    category VARCHAR(50) NOT NULL,
    difficulty INTEGER NOT NULL DEFAULT 3,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_locations_active ON locations(active);
CREATE INDEX idx_locations_category ON locations(category);