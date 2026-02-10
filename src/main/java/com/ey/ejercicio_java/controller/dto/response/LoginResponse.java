package com.ey.ejercicio_java.controller.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record LoginResponse(
        UUID id,
        String name,
        String email,
        LocalDateTime lastLogin,
        String token,
        Boolean isActive
) {
}
