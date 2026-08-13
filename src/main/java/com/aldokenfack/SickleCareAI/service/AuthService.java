package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.config.JwtUtils;
import com.aldokenfack.SickleCareAI.dto.UserLoginRequestDTO;
import com.aldokenfack.SickleCareAI.dto.UserLoginResponseDTO;
import com.aldokenfack.SickleCareAI.exception.UserNotFoundException;
import com.aldokenfack.SickleCareAI.model.User;
import com.aldokenfack.SickleCareAI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapperService userMapperService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    public UserLoginResponseDTO loginUser(UserLoginRequestDTO dto){

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("Bad email or password. Please try again !");
        }

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found !"));

        return userMapperService.mapToResponseDTO(user);
    }

}
