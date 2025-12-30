package com.sport.tazkarti.model.dto;

public record TeamResponse(
        Long id,
        String name,
        String logoUrl,
        String description
) { }
