package com.ey.ejercicio_java.controller;

import com.ey.ejercicio_java.controller.dto.request.LoginRequest;
import com.ey.ejercicio_java.controller.dto.request.RegisterUserRequest;
import com.ey.ejercicio_java.controller.dto.response.LoginResponse;
import com.ey.ejercicio_java.controller.dto.response.RegisterUserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/auth")
public interface AuthResource {

    @PostMapping(value = "/register", produces = "application/json", consumes = "application/json")
    ResponseEntity<RegisterUserResponse> registerUser(@RequestBody @Valid RegisterUserRequest registerUserRequest);

    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
    ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest);
}