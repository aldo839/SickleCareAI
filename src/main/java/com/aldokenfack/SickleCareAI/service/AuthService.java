package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.config.JwtUtils;
import com.aldokenfack.SickleCareAI.dto.UserLoginRequestDTO;
import com.aldokenfack.SickleCareAI.dto.UserLoginResponseDTO;
import com.aldokenfack.SickleCareAI.exception.UserNotFoundException;
import com.aldokenfack.SickleCareAI.model.JwtRefreshToken;
import com.aldokenfack.SickleCareAI.model.User;
import com.aldokenfack.SickleCareAI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapperService userMapperService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final JwtRefreshTokenService jwtRefreshTokenService;

    public UserLoginResponseDTO loginUser(UserLoginRequestDTO dto){

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found !"));

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Bad email or password. Please try again !");
        }

        String accessToken = jwtUtils.generateToken(user.getEmail());
        JwtRefreshToken refreshToken = jwtRefreshTokenService.generateRefreshToken(user.getId());

        return userMapperService.mapToResponseDTO(user, accessToken, refreshToken.getToken());
    }


    public User getCurrentUser(){

        // Fetch the email inside the JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()){
            throw new BadCredentialsException("User not authenticated");
        }

        String email = authentication.getName();
        // Fetch the user with this email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));
    }

}
