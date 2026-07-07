package com.albaniaguessr.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ScoreCalculator {

    /**
     * Calculates map score based on distance from correct location.
     * Formula: 5000 * e^(-distanceKm / 200)
     * Max: 5000 (when distance = 0)
     *
     * @param distanceKm Great-circle distance in kilometers
     * @return Map score (0-5000)
     */
    public static int calculateMapScore(double distanceKm) {
        return (int) Math.round(5000 * Math.exp(-distanceKm / 200.0));
    }

    /**
     * Calculates year score based on difference from correct year.
     * Formula: max(5000 - |yearDiff| * 100, 0)
     * Max: 5000 (when yearDiff = 0)
     *
     * @param guessedYear The player's guessed year
     * @param correctYear The correct year
     * @return Year score (0-5000)
     */
    public static int calculateYearScore(int guessedYear, int correctYear) {
        int yearDiff = Math.abs(guessedYear - correctYear);
        return Math.max(5000 - (yearDiff * 100), 0);
    }

    /**
     * Calculates total round score.
     *
     * @param distanceKm Distance from correct location in km
     * @param guessedYear Player's guessed year
     * @param correctYear Correct year
     * @return Total round score (0-10000)
     */
    public static int calculateRoundScore(double distanceKm, int guessedYear, int correctYear) {
        return calculateMapScore(distanceKm) + calculateYearScore(guessedYear, correctYear);
    }
}