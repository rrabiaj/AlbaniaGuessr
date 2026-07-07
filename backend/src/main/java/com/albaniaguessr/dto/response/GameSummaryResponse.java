package com.albaniaguessr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSummaryResponse {
    private String sessionId;
    private String mode;
    private Integer totalScore;
    private Boolean completed;
    private Integer totalRounds;
    private List<RoundSummary> rounds;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoundSummary {
        private Integer roundNumber;
        private String locationName;
        private Double correctLat;
        private Double correctLng;
        private Integer correctYear;
        private Double guessedLat;
        private Double guessedLng;
        private Integer guessedYear;
        private Double distanceKm;
        private Integer yearDiff;
        private Integer mapScore;
        private Integer yearScore;
        private Integer roundScore;
    }
}