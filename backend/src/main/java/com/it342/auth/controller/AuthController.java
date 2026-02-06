package com.it342.auth.controller;

import com.it342.auth.dto.AuthResponse;
import com.it342.auth.dto.LoginRequest;
import com.it342.auth.dto.RegisterRequest;
import com.it342.auth.dto.UserDTO;
import com.it342.auth.model.User;
import com.it342.auth.repository.UserRepository;
import com.it342.auth.service.AuthService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, null, "Passwords do not match.", false));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, null, "Email already registered.", false));
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, null, "Username already taken.", false));
        }

        User registered = authService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
        UserDTO userDTO = authService.toDTO(registered);
        String token = authService.generateToken(registered);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, userDTO, "Registration successful.", true));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> authenticated = authService.authenticateUser(request.getEmail(), request.getPassword());
        if (authenticated.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, "Invalid email or password.", false));
        }
        User user = authenticated.get();
        String token = authService.generateToken(user);
        UserDTO userDTO = authService.toDTO(user);
        return ResponseEntity.ok(new AuthResponse(token, userDTO, "Login successful.", true));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {
        return ResponseEntity.ok(new AuthResponse(null, null, "Logout successful.", true));
    }
}
