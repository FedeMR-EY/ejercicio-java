package com.ey.ejercicio_java.controller;

import com.ey.ejercicio_java.controller.dto.request.LoginRequest;
import com.ey.ejercicio_java.controller.dto.request.RegisterUserRequest;
import com.ey.ejercicio_java.controller.dto.response.LoginResponse;
import com.ey.ejercicio_java.controller.dto.response.RegisterUserResponse;
import com.ey.ejercicio_java.service.business.AuthBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthResource {

    private final AuthBusinessService authBusinessService;

    @Override
    public ResponseEntity<RegisterUserResponse> registerUser(RegisterUserRequest registerUserRequest) {
        return authBusinessService.registerUser(registerUserRequest);
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        return authBusinessService.login(loginRequest);
    }
}
