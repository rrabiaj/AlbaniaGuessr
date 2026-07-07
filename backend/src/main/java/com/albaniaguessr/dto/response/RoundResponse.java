package com.albaniaguessr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoundResponse {
    private String sessionId;
    private Integer roundNumber;
    private Integer totalRounds;
    private String imageUrl;
    private String locationName;
    private Integer difficulty;
    private String category;
    // NOTE: Coordinates are deliberately NOT included here.
    // They are only revealed AFTER the player submits a guess.
}