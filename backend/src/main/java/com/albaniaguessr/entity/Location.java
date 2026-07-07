package com.albaniaguessr.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Column
    private String thumbnailUrl;

    @Column(nullable = false)
    private Double correctLat;

    @Column(nullable = false)
    private Double correctLng;

    @Column(nullable = false)
    private Integer correctYear;

    @Column(nullable = false)
    private String locationName;

    @Column(length = 1000)
    private String funFact;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LocationCategory category;

    @Column(nullable = false)
    @Builder.Default
    private Integer difficulty = 3;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    public enum LocationCategory {
        HISTORIC, MODERN, LANDMARK, NATURE, SPORTS
    }
}