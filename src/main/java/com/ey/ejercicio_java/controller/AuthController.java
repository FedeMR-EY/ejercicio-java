package com.ey.ejercicio_java.controller;

import com.ey.ejercicio_java.controller.dto.request.RegisterUserRequest;
import com.ey.ejercicio_java.controller.dto.response.RegisterUserResponse;
import com.ey.ejercicio_java.service.business.AuthBusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthResource {

    private final AuthBusinessService authBusinessService;

    public AuthController(AuthBusinessService authBusinessService) {
        this.authBusinessService = authBusinessService;
    }

    @Override
    public ResponseEntity<RegisterUserResponse> registerUser(RegisterUserRequest registerUserRequest) {
        return authBusinessService.registerUser(registerUserRequest);
    }
}
