package com.it342.auth.service;

import com.it342.auth.dto.UserDTO;
import com.it342.auth.model.User;
import com.it342.auth.repository.UserRepository;
import com.it342.auth.security.JWTTokenProvider;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public User registerUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public Optional<User> authenticateUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()));
    }

    public String generateToken(User user) {
        return tokenProvider.generateToken(user);
    }

    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId() != null ? user.getId().toString() : null,
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt());
    }
}
