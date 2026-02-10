package com.ey.ejercicio_java.controller;

import com.ey.ejercicio_java.controller.dto.request.RegisterUserRequest;
import com.ey.ejercicio_java.controller.dto.response.RegisterUserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthResource {
    @PostMapping(value = "v1/auth/register", produces = "application/json", consumes = "application/json")
    ResponseEntity<RegisterUserResponse> registerUser(@RequestBody @Valid RegisterUserRequest registerUserRequest);
}