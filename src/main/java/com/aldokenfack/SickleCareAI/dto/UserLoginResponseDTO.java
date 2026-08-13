package com.aldokenfack.SickleCareAI.dto;

import com.aldokenfack.SickleCareAI.model.Role;

public record UserLoginResponseDTO(

        String token,

        String username,

        Role role

) {
}
