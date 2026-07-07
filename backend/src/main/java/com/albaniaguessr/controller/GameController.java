package com.albaniaguessr.controller;

import com.albaniaguessr.dto.request.GuessRequest;
import com.albaniaguessr.dto.request.StartGameRequest;
import com.albaniaguessr.dto.response.GameSummaryResponse;
import com.albaniaguessr.dto.response.RoundResponse;
import com.albaniaguessr.dto.response.RoundResultResponse;
import com.albaniaguessr.service.DailyChallengeService;
import com.albaniaguessr.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;
    private final DailyChallengeService dailyChallengeService;

    /**
     * Start a new game session.
     * POST /api/games/start
     */
    @PostMapping("/start")
    public ResponseEntity<GameSummaryResponse> startGame(@RequestBody(required = false) @Valid StartGameRequest request) {
        String mode = request != null ? request.getMode() : "CLASSIC";
        String sessionToken = request != null ? request.getSessionToken() : null;
        GameSummaryResponse response = gameService.startGame(mode, sessionToken);
        return ResponseEntity.ok(response);
    }

    /**
     * Get round data (NO correct coordinates!).
     * GET /api/games/{sessionId}/round/{n}
     */
    @GetMapping("/{sessionId}/round/{n}")
    public ResponseEntity<RoundResponse> getRound(
            @PathVariable String sessionId,
            @PathVariable int n) {
        try {
            UUID uuid = UUID.fromString(sessionId);
            RoundResponse response = gameService.getRound(uuid, n);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Submit a guess for a round.
     * POST /api/games/{sessionId}/round/{n}/guess
     */
    @PostMapping("/{sessionId}/round/{n}/guess")
    public ResponseEntity<RoundResultResponse> submitGuess(
            @PathVariable String sessionId,
            @PathVariable int n,
            @Valid @RequestBody GuessRequest request) {
        try {
            UUID uuid = UUID.fromString(sessionId);
            RoundResultResponse response = gameService.submitGuess(
                    uuid, n,
                    request.getGuessedLat(),
                    request.getGuessedLng(),
                    request.getGuessedYear(),
                    request.getTimeTakenSeconds());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Get full game summary.
     * GET /api/games/{sessionId}/summary
     */
    @GetMapping("/{sessionId}/summary")
    public ResponseEntity<GameSummaryResponse> getSummary(@PathVariable String sessionId) {
        try {
            UUID uuid = UUID.fromString(sessionId);
            GameSummaryResponse response = gameService.getSummary(uuid);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}