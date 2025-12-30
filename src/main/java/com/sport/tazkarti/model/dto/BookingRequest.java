package com.sport.tazkarti.model.dto;

import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull(message = "Match ID is required")
        Long matchId
) {}
