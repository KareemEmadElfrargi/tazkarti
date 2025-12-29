package com.sport.tazkarti.model.dto;

public record UserResponse(
        Long id,
        String username,
        String email,
        String fanId,
        String role
) {}
