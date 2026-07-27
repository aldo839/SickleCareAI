package com.aldokenfack.SickleCareAI.dto;

import com.aldokenfack.SickleCareAI.validator.ValidPassword;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PatientUpdateDTO {

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

    @Positive(message = "Weight must be positive !")
    @Max(value = 300, message = "Weight must be less that 300 (Kg)")
    private Double weight;

    @Size(max = 20, message = "Region must be less than 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\d\\s]$", message = "Region must only contain letter, number and space")
    private String region;

    @Size(max = 20, message = "City must be less than 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\d\\s]$", message = "Region must only contain letter, number and space")
    private String city;

}
