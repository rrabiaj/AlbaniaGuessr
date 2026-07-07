CREATE TABLE IF NOT EXISTS round_results (
    id BIGSERIAL PRIMARY KEY,
    game_session_id UUID NOT NULL,
    location_id BIGINT NOT NULL,
    guessed_lat DOUBLE PRECISION NOT NULL,
    guessed_lng DOUBLE PRECISION NOT NULL,
    guessed_year INTEGER NOT NULL,
    distance_km DOUBLE PRECISION NOT NULL,
    year_diff INTEGER NOT NULL,
    map_score INTEGER NOT NULL,
    year_score INTEGER NOT NULL,
    round_score INTEGER NOT NULL,
    time_taken_seconds BIGINT,
    CONSTRAINT fk_round_game_session FOREIGN KEY (game_session_id) REFERENCES game_sessions(id),
    CONSTRAINT fk_round_location FOREIGN KEY (location_id) REFERENCES locations(id)
);

CREATE INDEX idx_round_results_game_session ON round_results(game_session_id);