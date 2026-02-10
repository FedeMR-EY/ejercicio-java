package com.ey.ejercicio_java.controller.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record RegisterUserResponse(UUID uuid, LocalDate created, LocalDate modified, LocalDate lastLogin, String token, Boolean isActive) {
}
