package com.albaniaguessr.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GuessRequest {
    @NotNull
    private Double guessedLat;

    @NotNull
    private Double guessedLng;

    @NotNull
    private Integer guessedYear;

    private Integer timeTakenSeconds;
}