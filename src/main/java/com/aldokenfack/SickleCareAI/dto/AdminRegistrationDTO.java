package com.aldokenfack.SickleCareAI.dto;

import com.aldokenfack.SickleCareAI.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminRegistrationDTO {

    @NotBlank(message = "Username is require !")
    @Pattern(regexp = "^[a-zA-Z\\d]$", message = "Username must only contain letter")
    @Size(min = 4, max = 20, message = "Username must have between 4 and 20 characters !")
    private String username;

    @Email(message = "Email must be valid !")
    @NotBlank(message = "Email is require !")
    private String email;

    @NotBlank(message = "Password is require !")
    @Size(min = 8, message = "Password must have minimum 8 characters !")
    @ValidPassword(message = "Invalid password. Password must be very long or contain small and capital letters, number and specific character")
    private String password;

    @Size(max = 20, message = "Firstname must have less than 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\d]$", message = "Firstname must only contain letter")
    private String firstname;

    @Size(max = 20, message = "Lastname must have less than 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\d]$", message = "Firstname must only contain letter")
    private String lastname;

}
