package com.albaniaguessr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String sessionToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private GameMode mode = GameMode.CLASSIC;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalScore = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean completed = false;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RoundResult> rounds = new ArrayList<>();

    public enum GameMode {
        CLASSIC, DAILY_CHALLENGE, TIMED
    }
}