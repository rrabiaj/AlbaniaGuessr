package com.albaniaguessr.service;

import com.albaniaguessr.entity.GameSession;
import com.albaniaguessr.entity.Location;
import com.albaniaguessr.entity.RoundResult;
import com.albaniaguessr.dto.response.*;
import com.albaniaguessr.service.store.InMemoryGameStore;
import com.albaniaguessr.service.store.InMemoryLocationStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final InMemoryGameStore gameStore;
    private final InMemoryLocationStore locationStore;
    private final ScoringService scoringService;

    private static final int ROUNDS_PER_GAME = 5;

    /**
     * Start a new game session.
     */
    public GameSummaryResponse startGame(String mode, String sessionToken) {
        // Validate mode
        GameSession.GameMode gameMode;
        try {
            gameMode = GameSession.GameMode.valueOf(mode != null ? mode.toUpperCase() : "CLASSIC");
        } catch (IllegalArgumentException e) {
            gameMode = GameSession.GameMode.CLASSIC;
        }

        // Generate session token
        String token = sessionToken != null ? sessionToken : UUID.randomUUID().toString();

        // Create game session
        GameSession session = GameSession.builder()
                .sessionToken(token)
                .mode(gameMode)
                .totalScore(0)
                .completed(false)
                .startedAt(LocalDateTime.now())
                .build();

        // Select 5 random locations for the game
        List<Location> roundLocations = locationStore.findRandomActive(ROUNDS_PER_GAME);

        // Create round results (without guess data yet)
        for (int i = 0; i < roundLocations.size(); i++) {
            RoundResult round = RoundResult.builder()
                    .gameSession(session)
                    .location(roundLocations.get(i))
                    .roundNumber(i + 1)
                    .build();
            session.getRounds().add(round);
        }

        gameStore.save(session);

        log.debug("Game started: sessionId={}, mode={}", session.getId(), gameMode);

        return GameSummaryResponse.builder()
                .sessionId(session.getId().toString())
                .mode(gameMode.name())
                .totalScore(0)
                .completed(false)
                .totalRounds(ROUNDS_PER_GAME)
                .rounds(new ArrayList<>())
                .build();
    }

    /**
     * Get round data (no coordinates revealed).
     */
    public RoundResponse getRound(UUID sessionId, int roundNumber) {
        GameSession session = gameStore.findById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Game session not found: " + sessionId);
        }

        if (roundNumber < 1 || roundNumber > ROUNDS_PER_GAME) {
            throw new IllegalArgumentException("Invalid round number: " + roundNumber);
        }

        RoundResult round = session.getRounds().stream()
                .filter(r -> r.getRoundNumber() == roundNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Round not found: " + roundNumber));

        Location location = round.getLocation();

        // CRITICAL: Do NOT include lat/lng in this response!
        return RoundResponse.builder()
                .sessionId(sessionId.toString())
                .roundNumber(roundNumber)
                .totalRounds(ROUNDS_PER_GAME)
                .imageUrl(location.getImageUrl())
                .locationName(location.getLocationName())
                .difficulty(location.getDifficulty())
                .category(location.getCategory().name())
                .build();
    }

    /**
     * Submit a guess for a round and get scoring result.
     */
    public RoundResultResponse submitGuess(UUID sessionId, int roundNumber,
                                            double guessedLat, double guessedLng,
                                            int guessedYear, Integer timeTakenSeconds) {
        GameSession session = gameStore.findById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Game session not found: " + sessionId);
        }

        RoundResult round = session.getRounds().stream()
                .filter(r -> r.getRoundNumber() == roundNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Round not found: " + roundNumber));

        if (round.getRoundScore() != null) {
            throw new IllegalStateException("Round " + roundNumber + " has already been guessed");
        }

        Location location = round.getLocation();

        // Calculate distance and scores
        double distanceKm = scoringService.calculateDistance(
                guessedLat, guessedLng, location.getCorrectLat(), location.getCorrectLng());
        int yearDiff = Math.abs(guessedYear - location.getCorrectYear());
        int mapScore = scoringService.calculateMapScore(distanceKm);
        int yearScore = scoringService.calculateYearScore(guessedYear, location.getCorrectYear());
        int roundScore = mapScore + yearScore;

        // Update round result
        round.setGuessedLat(guessedLat);
        round.setGuessedLng(guessedLng);
        round.setGuessedYear(guessedYear);
        round.setDistanceKm(distanceKm);
        round.setYearDiff(yearDiff);
        round.setMapScore(mapScore);
        round.setYearScore(yearScore);
        round.setRoundScore(roundScore);
        round.setTimeTakenSeconds(timeTakenSeconds != null ? timeTakenSeconds : 0);

        // Update session total score
        session.setTotalScore(session.getTotalScore() + roundScore);

        // Check if game is completed
        boolean allGuessed = session.getRounds().stream()
                .allMatch(r -> r.getRoundScore() != null);
        if (allGuessed) {
            session.setCompleted(true);
            session.setCompletedAt(LocalDateTime.now());
        }

        gameStore.save(session);

        log.debug("Round {} submitted: distance={}km, mapScore={}, yearScore={}, roundScore={}",
                roundNumber, String.format("%.2f", distanceKm), mapScore, yearScore, roundScore);

        return RoundResultResponse.builder()
                .sessionId(sessionId.toString())
                .roundNumber(roundNumber)
                .guessedLat(guessedLat)
                .guessedLng(guessedLng)
                .guessedYear(guessedYear)
                .correctLat(location.getCorrectLat())
                .correctLng(location.getCorrectLng())
                .correctYear(location.getCorrectYear())
                .locationName(location.getLocationName())
                .funFact(location.getFunFact())
                .distanceKm(distanceKm)
                .yearDiff(yearDiff)
                .mapScore(mapScore)
                .yearScore(yearScore)
                .roundScore(roundScore)
                .build();
    }

    /**
     * Get full game summary.
     */
    public GameSummaryResponse getSummary(UUID sessionId) {
        GameSession session = gameStore.findById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Game session not found: " + sessionId);
        }

        List<GameSummaryResponse.RoundSummary> roundSummaries = session.getRounds().stream()
                .map(round -> {
                    Location location = round.getLocation();
                    return GameSummaryResponse.RoundSummary.builder()
                            .roundNumber(round.getRoundNumber())
                            .locationName(location.getLocationName())
                            .correctLat(location.getCorrectLat())
                            .correctLng(location.getCorrectLng())
                            .correctYear(location.getCorrectYear())
                            .guessedLat(round.getGuessedLat())
                            .guessedLng(round.getGuessedLng())
                            .guessedYear(round.getGuessedYear())
                            .distanceKm(round.getDistanceKm())
                            .yearDiff(round.getYearDiff())
                            .mapScore(round.getMapScore())
                            .yearScore(round.getYearScore())
                            .roundScore(round.getRoundScore())
                            .build();
                })
                .collect(Collectors.toList());

        return GameSummaryResponse.builder()
                .sessionId(session.getId().toString())
                .mode(session.getMode().name())
                .totalScore(session.getTotalScore())
                .completed(session.getCompleted())
                .totalRounds(ROUNDS_PER_GAME)
                .rounds(roundSummaries)
                .build();
    }
}