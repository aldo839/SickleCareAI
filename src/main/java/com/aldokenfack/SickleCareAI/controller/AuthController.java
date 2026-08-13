package com.aldokenfack.SickleCareAI.controller;

import com.aldokenfack.SickleCareAI.dto.UserLoginRequestDTO;
import com.aldokenfack.SickleCareAI.dto.UserLoginResponseDTO;
import com.aldokenfack.SickleCareAI.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser(@Valid @RequestBody UserLoginRequestDTO dto){

        UserLoginResponseDTO userResponseDTO = authService.loginUser(dto);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

}
