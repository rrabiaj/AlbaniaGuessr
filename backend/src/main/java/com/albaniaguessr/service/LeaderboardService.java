package com.albaniaguessr.service;

import com.albaniaguessr.dto.response.LeaderboardEntry;
import com.albaniaguessr.service.store.InMemoryGameStore;
import com.albaniaguessr.service.store.InMemoryUserStore;
import com.albaniaguessr.entity.GameSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final InMemoryGameStore gameStore;
    private final InMemoryUserStore userStore;

    /**
     * Get global top scores.
     * In MVP mode, this uses in-memory completed games.
     */
    public List<LeaderboardEntry> getGlobalLeaderboard(int limit) {
        List<GameSession> completedGames = gameStore.findAll().stream()
                .filter(GameSession::getCompleted)
                .collect(Collectors.toList());

        // Group by user/session token and get max score
        Map<String, LeaderboardEntry> bestScores = new LinkedHashMap<>();

        for (GameSession session : completedGames) {
            String identifier;
            String username;

            if (session.getUser() != null) {
                identifier = "user_" + session.getUser().getId();
                username = session.getUser().getUsername();
            } else {
                identifier = "guest_" + session.getSessionToken();
                username = "Guest";
            }

            int score = session.getTotalScore() != null ? session.getTotalScore() : 0;
            Long userId = session.getUser() != null ? session.getUser().getId() : null;

            LeaderboardEntry existing = bestScores.get(identifier);
            if (existing == null || score > existing.getScore()) {
                bestScores.put(identifier, LeaderboardEntry.builder()
                        .username(username)
                        .score(score)
                        .userId(userId)
                        .build());
            }
        }

        // Sort by score descending
        List<LeaderboardEntry> sorted = new ArrayList<>(bestScores.values());
        sorted.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        // Assign ranks
        List<LeaderboardEntry> result = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, sorted.size()); i++) {
            LeaderboardEntry entry = sorted.get(i);
            entry.setRank(i + 1);
            result.add(entry);
        }

        return result;
    }

    /**
     * Get top scores for today's daily challenge.
     */
    public List<LeaderboardEntry> getDailyLeaderboard(String date, int limit) {
        // In MVP mode, return global leaderboard as fallback
        // Daily-specific leaderboard will be implemented in Phase 3 with Redis
        return getGlobalLeaderboard(limit);
    }
}