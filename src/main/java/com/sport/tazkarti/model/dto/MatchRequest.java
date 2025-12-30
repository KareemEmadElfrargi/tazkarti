package com.sport.tazkarti.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record MatchRequest(
        @NotNull(message = "Home team ID is required")
        Long homeTeamId,

        @NotNull(message = "Away team ID is required")
        Long awayTeamId,

        @NotNull(message = "Stadium ID is required")
        Long stadiumId,

        @NotNull(message = "Match time is required")
        @Future(message = "Match time must be in the future")
        LocalDateTime matchTime,

        @Positive(message = "Ticket price must be positive")
        Double ticketPrice
) {}
