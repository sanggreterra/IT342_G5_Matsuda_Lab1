package com.it342.timesheets.service;

import com.it342.timesheets.dto.*;
import com.it342.timesheets.entity.User;
import com.it342.timesheets.entity.UserSession;
import com.it342.timesheets.repository.UserRepository;
import com.it342.timesheets.repository.UserSessionRepository;
import com.it342.timesheets.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final UserSessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       UserSessionRepository sessionRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("User already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUserId());
        saveSession(user.getUserId(), token);

        return new AuthResponse(token, toUserResponse(user));
    }

    public AuthResponse login(LoginRequest request) {
        String input = request.getUsername().trim();
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(input, input);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = userOpt.get();
        if (!user.getIsActive()) {
            throw new RuntimeException("Account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            user.setFailedAttempts((user.getFailedAttempts() == null ? 0 : user.getFailedAttempts()) + 1);
            if (user.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
                user.setIsActive(false);
                userRepository.save(user);
                throw new RuntimeException("Account locked");
            }
            userRepository.save(user);
            throw new RuntimeException("Invalid credentials");
        }

        user.setFailedAttempts(0);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUserId());
        saveSession(user.getUserId(), token);

        return new AuthResponse(token, toUserResponse(user));
    }

    public void logout(String token) {
        sessionRepository.findBySessionToken(token).ifPresent(session -> {
            session.setIsActive(false);
            sessionRepository.save(session);
        });
    }

    public UserResponse getCurrentUser(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }
        Integer userId = jwtUtil.getUserIdFromToken(token);
        return userRepository.findById(userId)
                .map(this::toUserResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void saveSession(Integer userId, String token) {
        UserSession session = new UserSession();
        session.setUserId(userId);
        session.setSessionToken(token);
        session.setExpiresAt(LocalDateTime.now().plusHours(24));
        sessionRepository.save(session);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getUserId(), user.getUsername(), user.getEmail());
    }
}
