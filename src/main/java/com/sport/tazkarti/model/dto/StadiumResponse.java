package com.sport.tazkarti.model.dto;

public record StadiumResponse(
        Long id,
        String name,
        String city,
        Integer capacity,
        boolean active
) {}
