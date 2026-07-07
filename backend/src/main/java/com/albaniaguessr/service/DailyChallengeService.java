package com.albaniaguessr.service;

import com.albaniaguessr.entity.DailyChallenge;
import com.albaniaguessr.entity.GameSession;
import com.albaniaguessr.entity.Location;
import com.albaniaguessr.service.store.InMemoryLocationStore;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyChallengeService {

    private final InMemoryLocationStore locationStore;
    private final GameService gameService;

    // In-memory store for daily challenges
    private final Map<LocalDate, DailyChallenge> challenges = new ConcurrentHashMap<>();
    // Track submitted scores (sessionId -> score)
    private final Map<String, Integer> dailyScores = new ConcurrentHashMap<>();
    // Daily leaderboard: date -> list of (playerName, score)
    private final Map<LocalDate, List<DailyScoreEntry>> dailyLeaderboard = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // Pre-generate today's challenge
        getOrCreateTodayChallenge();
    }

    /**
     * Get or create today's daily challenge.
     */
    public DailyChallenge getOrCreateTodayChallenge() {
        LocalDate today = LocalDate.now();
        return challenges.computeIfAbsent(today, date -> {
            List<Location> locations = locationStore.findRandomActive(5);
            DailyChallenge challenge = DailyChallenge.builder()
                    .challengeDate(date)
                    .locations(locations)
                    .build();
            log.info("Created daily challenge for {} with {} locations", date, locations.size());
            return challenge;
        });
    }

    /**
     * Get today's challenge locations (IDs only, no coordinates).
     */
    public List<Long> getTodayChallengeLocationIds() {
        DailyChallenge challenge = getOrCreateTodayChallenge();
        return challenge.getLocations().stream()
                .map(Location::getId)
                .toList();
    }

    /**
     * Record a daily challenge score.
     */
    public void recordDailyScore(String playerName, int score) {
        LocalDate today = LocalDate.now();
        dailyLeaderboard.computeIfAbsent(today, k -> new ArrayList<>())
                .add(new DailyScoreEntry(playerName, score, System.currentTimeMillis()));
    }

    /**
     * Get today's daily leaderboard.
     */
    public List<DailyScoreEntry> getTodayLeaderboard() {
        LocalDate today = LocalDate.now();
        List<DailyScoreEntry> entries = dailyLeaderboard.getOrDefault(today, new ArrayList<>());
        entries.sort((a, b) -> Integer.compare(b.score, a.score));
        return entries;
    }

    public record DailyScoreEntry(String playerName, int score, long timestamp) {}
}