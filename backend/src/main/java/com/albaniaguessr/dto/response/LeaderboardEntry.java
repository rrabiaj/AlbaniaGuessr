package com.albaniaguessr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntry {
    private Integer rank;
    private String username;
    private Integer score;
    private Long userId;
}