package com.albaniaguessr.service;

import com.albaniaguessr.util.HaversineDistanceCalculator;
import com.albaniaguessr.util.ScoreCalculator;
import org.springframework.stereotype.Service;

@Service
public class ScoringService {

    /**
     * Calculate the distance between two geographic coordinates using Haversine formula.
     */
    public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        return HaversineDistanceCalculator.calculate(lat1, lng1, lat2, lng2);
    }

    /**
     * Calculate map score (0-5000) based on distance from correct location.
     * Formula: 5000 * e^(-distanceKm / 200)
     */
    public int calculateMapScore(double distanceKm) {
        return ScoreCalculator.calculateMapScore(distanceKm);
    }

    /**
     * Calculate year score (0-5000) based on difference from correct year.
     * Formula: max(5000 - |yearDiff| * 100, 0)
     */
    public int calculateYearScore(int guessedYear, int correctYear) {
        return ScoreCalculator.calculateYearScore(guessedYear, correctYear);
    }

    /**
     * Calculate total round score (0-10000).
     */
    public int calculateRoundScore(double distanceKm, int guessedYear, int correctYear) {
        return ScoreCalculator.calculateRoundScore(distanceKm, guessedYear, correctYear);
    }
}