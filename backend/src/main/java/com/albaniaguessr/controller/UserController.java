package com.albaniaguessr.controller;

import com.albaniaguessr.service.store.InMemoryUserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final InMemoryUserStore userStore;

    /**
     * Get current user stats.
     * GET /api/users/me/stats
     */
    @GetMapping("/me/stats")
    public ResponseEntity<?> getUserStats() {
        // In MVP mode, return placeholder stats
        return ResponseEntity.ok(Map.of(
                "gamesPlayed", 0,
                "bestScore", 0,
                "message", "Full stats will be available in Phase 3 with JWT auth"
        ));
    }
}