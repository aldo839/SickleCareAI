package com.aldokenfack.SickleCareAI.controller;

import com.aldokenfack.SickleCareAI.annotation.RateLimit;
import com.aldokenfack.SickleCareAI.config.JwtUtils;
import com.aldokenfack.SickleCareAI.dto.ForgotPasswordRequestDTO;
import com.aldokenfack.SickleCareAI.dto.ResetPasswordRequestDTO;
import com.aldokenfack.SickleCareAI.dto.UserLoginRequestDTO;
import com.aldokenfack.SickleCareAI.dto.UserLoginResponseDTO;
import com.aldokenfack.SickleCareAI.repository.JwtRefreshTokenRepository;
import com.aldokenfack.SickleCareAI.service.AuthService;
import com.aldokenfack.SickleCareAI.service.JwtRefreshTokenService;
import com.aldokenfack.SickleCareAI.service.PasswordResetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Tag(name = "Authentications", description = "Account activation and user authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
    private final JwtRefreshTokenService jwtRefreshTokenService;
    private final JwtUtils jwtUtils;
    private final PasswordResetService passwordResetService;


    @PostMapping("/activate-account")
    public ResponseEntity<String> activateAccount(@RequestBody Map<String, String> requestBody){

        authService.activation(requestBody);

        return ResponseEntity.ok("Account successfully activate.");
    }


    @RateLimit(attemps = 5, period = 1, unit = TimeUnit.MINUTES, keyType = "SUBNET+USER")
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser(@Valid @RequestBody UserLoginRequestDTO dto){

        UserLoginResponseDTO userResponseDTO = authService.loginUser(dto);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload){
        String refreshToken = payload.get("refreshToken");

        return jwtRefreshTokenRepository.findByToken(refreshToken)
                .map(token -> {
                    if (jwtRefreshTokenService.isTokenExpired(token)){
                        jwtRefreshTokenRepository.delete(token);
                        return ResponseEntity.badRequest().body("Refresh token expired. Please login again.");
                    }
                    String newJwt = jwtUtils.generateToken(token.getUser().getEmail());
                    return ResponseEntity.ok(Map.of("token", newJwt));
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> payload){
        String requestToken = payload.get("refreshToken");

        if (requestToken == null || requestToken.isBlank()){
            return ResponseEntity.badRequest().body("Refresh token is required.");
        }

        return jwtRefreshTokenRepository.findByToken(requestToken)
                .map(token -> {
                    jwtRefreshTokenRepository.delete(token);
                    return ResponseEntity.ok("Logged out successfully.");
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDTO dto){

        passwordResetService.processForgotPassword(dto);

        return ResponseEntity.ok("Password reset link send to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO dto){

        passwordResetService.updatePassword(dto);

        return ResponseEntity.ok("Password reset successful.");
    }

}
