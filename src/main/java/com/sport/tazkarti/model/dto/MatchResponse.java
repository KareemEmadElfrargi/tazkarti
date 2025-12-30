package com.sport.tazkarti.model.dto;

import java.time.LocalDateTime;

public record MatchResponse(
        Long id,
        TeamResponse homeTeam,
        TeamResponse awayTeam,
        StadiumResponse stadium,
        LocalDateTime matchTime,
        Double ticketPrice
) {}