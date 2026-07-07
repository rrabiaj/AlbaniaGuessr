package com.albaniaguessr.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "round_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoundResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_session_id", nullable = false)
    private GameSession gameSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private Integer roundNumber;

    @Column
    private Double guessedLat;

    @Column
    private Double guessedLng;

    @Column
    private Integer guessedYear;

    @Column
    private Double distanceKm;

    @Column
    private Integer yearDiff;

    @Column
    private Integer mapScore;

    @Column
    private Integer yearScore;

    @Column
    private Integer roundScore;

    @Column
    private Integer timeTakenSeconds;
}