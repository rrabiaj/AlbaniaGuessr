package com.albaniaguessr.dto.request;

import lombok.Data;

@Data
public class StartGameRequest {
    private String mode; // CLASSIC, DAILY_CHALLENGE, TIMED
    private String sessionToken; // for guest play, null for new session
}