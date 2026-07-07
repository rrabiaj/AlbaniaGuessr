package com.albaniaguessr.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HaversineDistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Calculates the great-circle distance between two points on Earth using
     * the Haversine formula.
     *
     * @param lat1 Latitude of point 1 in degrees
     * @param lng1 Longitude of point 1 in degrees
     * @param lat2 Latitude of point 2 in degrees
     * @param lng2 Longitude of point 2 in degrees
     * @return Distance in kilometers
     */
    public static double calculate(double lat1, double lng1, double lat2, double lng2) {
        double lat1Rad = Math.toRadians(lat1);
        double lng1Rad = Math.toRadians(lng1);
        double lat2Rad = Math.toRadians(lat2);
        double lng2Rad = Math.toRadians(lng2);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLng = lng2Rad - lng1Rad;

        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                 + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(deltaLng / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}