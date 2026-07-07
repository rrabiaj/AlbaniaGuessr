package com.albaniaguessr.controller;

import com.albaniaguessr.dto.response.AuthResponse;
import com.albaniaguessr.entity.User;
import com.albaniaguessr.security.JwtTokenProvider;
import com.albaniaguessr.service.store.InMemoryUserStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final InMemoryUserStore userStore;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(InMemoryUserStore userStore,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.userStore = userStore;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Validate
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username is required"));
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 6 characters"));
        }

        // Check if username exists
        if (userStore.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Username already exists"));
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        userStore.save(user);

        String token = jwtTokenProvider.generateToken(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AuthResponse.builder()
                        .token(token)
                        .userId(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var userOpt = userStore.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        String token = jwtTokenProvider.generateToken(user);

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build());
    }

    // Inner request classes (avoid circular deps)
    public record RegisterRequest(String username, String email, String password) {}
    public record LoginRequest(String username, String password) {}
}