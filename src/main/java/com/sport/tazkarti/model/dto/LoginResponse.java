package com.sport.tazkarti.model.dto;

public record LoginResponse(
        String token,
        String email,
        String fanId
) {}
