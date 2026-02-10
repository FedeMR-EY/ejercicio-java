package com.ey.ejercicio_java.service.business;

import com.ey.ejercicio_java.controller.dto.request.RegisterUserRequest;
import com.ey.ejercicio_java.controller.dto.response.RegisterUserResponse;
import com.ey.ejercicio_java.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthBusinessService {
    private final UserService userService;

    public AuthBusinessService(UserService userService) {
        this.userService = userService;
    }

    public ResponseEntity<RegisterUserResponse> registerUser(RegisterUserRequest registerUserRequest){
        return ResponseEntity.ok(RegisterUserResponse.builder().build());
    }
}
