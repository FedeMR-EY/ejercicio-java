package com.ey.ejercicio_java.controller.dto.request;

import com.ey.ejercicio_java.model.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.List;

@Builder
public record RegisterUserRequest(@NotEmpty String name, @NotEmpty @Email String email, @NotEmpty @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z].*[a-z])(?=.*\\d.*\\d)[A-Za-z\\d]+$") String password, @NotNull List<Phone> phones) {
}