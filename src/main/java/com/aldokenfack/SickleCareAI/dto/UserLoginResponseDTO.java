package com.aldokenfack.SickleCareAI.dto;

public record UserLoginResponseDTO(

        String username,

        String token,

        String refreshToken

) {
}
