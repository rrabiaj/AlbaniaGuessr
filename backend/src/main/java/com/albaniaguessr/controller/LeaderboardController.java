package com.albaniaguessr.controller;

import com.albaniaguessr.dto.response.LeaderboardEntry;
import com.albaniaguessr.service.DailyChallengeService;
import com.albaniaguessr.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;
    private final DailyChallengeService dailyChallengeService;

    /**
     * Get global leaderboard (top 100).
     * GET /api/leaderboard/global?limit=100
     */
    @GetMapping("/leaderboard/global")
    public ResponseEntity<List<LeaderboardEntry>> getGlobalLeaderboard(
            @RequestParam(defaultValue = "100") int limit) {
        List<LeaderboardEntry> leaderboard = leaderboardService.getGlobalLeaderboard(limit);
        return ResponseEntity.ok(leaderboard);
    }

    /**
     * Get daily leaderboard.
     * GET /api/leaderboard/daily?date=
     */
    @GetMapping("/leaderboard/daily")
    public ResponseEntity<List<LeaderboardEntry>> getDailyLeaderboard(
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "100") int limit) {
        List<LeaderboardEntry> leaderboard = leaderboardService.getDailyLeaderboard(date, limit);
        return ResponseEntity.ok(leaderboard);
    }

    /**
     * Get today's daily challenge.
     * GET /api/daily-challenge
     */
    @GetMapping("/daily-challenge")
    public ResponseEntity<Map<String, Object>> getDailyChallenge() {
        var locations = dailyChallengeService.getTodayChallengeLocationIds();
        var leaderboard = dailyChallengeService.getTodayLeaderboard();
        return ResponseEntity.ok(Map.of(
                "date", java.time.LocalDate.now().toString(),
                "locationIds", locations,
                "leaderboard", leaderboard
        ));
    }

    /**
     * Submit daily challenge score.
     * POST /api/daily-challenge/submit
     */
    @PostMapping("/daily-challenge/submit")
    public ResponseEntity<Map<String, String>> submitDailyChallenge(
            @RequestBody Map<String, Object> body) {
        String playerName = (String) body.getOrDefault("playerName", "Anonymous");
        int score = (int) body.getOrDefault("score", 0);
        dailyChallengeService.recordDailyScore(playerName, score);
        return ResponseEntity.ok(Map.of("status", "submitted"));
    }
}