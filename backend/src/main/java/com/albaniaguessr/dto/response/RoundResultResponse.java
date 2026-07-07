package com.albaniaguessr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoundResultResponse {
    private String sessionId;
    private Integer roundNumber;

    // Player's guess
    private Double guessedLat;
    private Double guessedLng;
    private Integer guessedYear;

    // Correct answer (revealed AFTER guess)
    private Double correctLat;
    private Double correctLng;
    private Integer correctYear;
    private String locationName;
    private String funFact;

    // Scoring breakdown
    private Double distanceKm;
    private Integer yearDiff;
    private Integer mapScore;
    private Integer yearScore;
    private Integer roundScore;
}