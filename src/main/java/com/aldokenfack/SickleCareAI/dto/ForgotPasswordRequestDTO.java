package com.aldokenfack.SickleCareAI.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ForgotPasswordRequestDTO {

    @NotBlank(message = "Email is require for reset password")
    @Email(message = "Email must valid")
    private String email;

}
