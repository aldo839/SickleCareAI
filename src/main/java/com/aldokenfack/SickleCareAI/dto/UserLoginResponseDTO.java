package com.aldokenfack.SickleCareAI.dto;

import com.aldokenfack.SickleCareAI.model.Role;

public record UserLoginResponseDTO(

        String username,

        Role role,

        String token,

        String refreshToken

) {
}
