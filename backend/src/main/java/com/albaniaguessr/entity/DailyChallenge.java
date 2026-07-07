package com.albaniaguessr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "daily_challenges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyChallenge {

    @Id
    @Column(nullable = false)
    private LocalDate challengeDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "daily_challenge_locations",
        joinColumns = @JoinColumn(name = "challenge_date"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    @Builder.Default
    private List<Location> locations = new ArrayList<>();
}