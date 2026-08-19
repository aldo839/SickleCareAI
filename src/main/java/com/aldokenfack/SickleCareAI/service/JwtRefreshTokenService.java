package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.exception.UserNotFoundException;
import com.aldokenfack.SickleCareAI.model.JwtRefreshToken;
import com.aldokenfack.SickleCareAI.model.User;
import com.aldokenfack.SickleCareAI.repository.JwtRefreshTokenRepository;
import com.aldokenfack.SickleCareAI.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtRefreshTokenService {

    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${app.expiration-time}")
    private Long expirationTime;

    @Transactional
    public JwtRefreshToken generateRefreshToken(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id : " + userId));

        JwtRefreshToken token = jwtRefreshTokenRepository.findByUserId(userId)
                        .orElseGet(JwtRefreshToken::new);

        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpirationTime(Instant.now().plusMillis(expirationTime));

        return jwtRefreshTokenRepository.save(token);
    }

    public boolean isTokenExpired(JwtRefreshToken token) {
        return token.getExpirationTime().isBefore(Instant.now());
    }

}
