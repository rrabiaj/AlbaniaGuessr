CREATE TABLE game_sessions (
    id UUID PRIMARY KEY,
    session_token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT REFERENCES users(id),
    mode VARCHAR(50) NOT NULL DEFAULT 'CLASSIC',
    total_score INTEGER NOT NULL DEFAULT 0,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    started_at TIMESTAMP NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMP
);

CREATE INDEX idx_game_sessions_user ON game_sessions(user_id);
CREATE INDEX idx_game_sessions_token ON game_sessions(session_token);

CREATE TABLE round_results (
    id BIGSERIAL PRIMARY KEY,
    game_session_id UUID NOT NULL REFERENCES game_sessions(id),
    location_id BIGINT NOT NULL REFERENCES locations(id),
    round_number INTEGER NOT NULL,
    guessed_lat DOUBLE PRECISION,
    guessed_lng DOUBLE PRECISION,
    guessed_year INTEGER,
    distance_km DOUBLE PRECISION,
    year_diff INTEGER,
    map_score INTEGER,
    year_score INTEGER,
    round_score INTEGER,
    time_taken_seconds INTEGER
);

CREATE INDEX idx_round_results_session ON round_results(game_session_id);