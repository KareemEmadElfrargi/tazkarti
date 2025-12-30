package com.sport.tazkarti.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StadiumRequest(
        @NotBlank(message = "Stadium name is required")
        String name,

        @NotBlank(message = "City is required")
        String city,

        @NotNull(message = "Capacity is required")
        @Min(value = 100, message = "Capacity must be at least 100")
        Integer capacity
) { }
