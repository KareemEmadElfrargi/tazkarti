package com.sport.tazkarti.model.dto;

import jakarta.validation.constraints.NotBlank;

public record TeamRequest(
        @NotBlank(message = "Team name is required")
        String name,

        @NotBlank(message = "Logo URL is required")
        String logoUrl,

        String description
) { }
