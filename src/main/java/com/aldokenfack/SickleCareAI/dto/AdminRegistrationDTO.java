package com.aldokenfack.SickleCareAI.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminRegistrationDTO {

    @NotBlank(message = "Username is require !")
    @Size(min = 4, max = 20, message = "Username must have between 4 and 20 characters !")
    private String username;

    @Email(message = "Email must be valid !")
    @NotBlank(message = "Email is require !")
    private String email;

    @NotBlank(message = "Password is require !")
    @Size(min = 8, message = "Password must have minimum 8 characters !")
    private String password;

    private String firstname;

    private String lastname;

}
