package com.aldokenfack.SickleCareAI.controller;

import com.aldokenfack.SickleCareAI.dto.UserLoginRequestDTO;
import com.aldokenfack.SickleCareAI.dto.UserResponseDTO;
import com.aldokenfack.SickleCareAI.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@RequestBody UserLoginRequestDTO dto){

        UserResponseDTO userResponseDTO = userService.loginUser(dto);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

}
