package com.ey.ejercicio_java.service.business;

import com.ey.ejercicio_java.controller.dto.request.LoginRequest;
import com.ey.ejercicio_java.controller.dto.request.RegisterUserRequest;
import com.ey.ejercicio_java.controller.dto.response.LoginResponse;
import com.ey.ejercicio_java.controller.dto.response.RegisterUserResponse;
import com.ey.ejercicio_java.exception.APIException;
import com.ey.ejercicio_java.model.APIError;
import com.ey.ejercicio_java.model.entity.Phone;
import com.ey.ejercicio_java.model.entity.User;
import com.ey.ejercicio_java.security.JwtService;
import com.ey.ejercicio_java.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthBusinessService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public ResponseEntity<RegisterUserResponse> registerUser(RegisterUserRequest request) {

        if (userService.existsByEmail(request.email())) {
            throw new APIException(APIError.builder()
                    .mensaje("El correo ya está registrado")
                    .build());
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setIsActive(true);

        if (request.phones() != null && !request.phones().isEmpty()) {
            List<Phone> phones = request.phones().stream()
                    .peek(phone -> phone.setUser(user))
                    .toList();
            user.setPhones(phones);
        }

        String token = jwtService.generateToken(user);
        user.setAccessToken(token);

        User savedUser = userService.save(user);

        RegisterUserResponse response = RegisterUserResponse.builder()
                .uuid(savedUser.getId())
                .created(savedUser.getCreatedAt().toLocalDate())
                .modified(savedUser.getModifiedAt().toLocalDate())
                .lastLogin(savedUser.getLastLogin().toLocalDate())
                .token(savedUser.getAccessToken())
                .isActive(savedUser.getIsActive())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new APIException(APIError.builder()
                    .mensaje("Credenciales inválidas")
                    .build());
        }

        User user = userService.findByEmail(request.email())
                .orElseThrow(() -> new APIException(APIError.builder()
                        .mensaje("Usuario no encontrado")
                        .build()));

        user.setLastLogin(LocalDateTime.now());
        userService.save(user);

        String token = jwtService.generateToken(user);

        LoginResponse response = LoginResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .lastLogin(user.getLastLogin())
                .token(token)
                .isActive(user.getIsActive())
                .build();

        return ResponseEntity.ok(response);
    }
}
