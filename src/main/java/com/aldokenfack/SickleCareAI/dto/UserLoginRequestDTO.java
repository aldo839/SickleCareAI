package com.aldokenfack.SickleCareAI.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserLoginRequestDTO {

    @NotBlank(message = "Email is require for authentication")
    @Email(message = "Email must valid")
    private String email;

    @NotBlank(message = "Password is require for authentication")
    private String password;

}
