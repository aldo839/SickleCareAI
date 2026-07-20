package com.aldokenfack.SickleCareAI.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PatientUpdateDTO {

    @Size(min = 4, max = 20, message = "Username must have minimum 4 characters and maximum 20 !")
    private String username;

    @Email(message = "Email must be valid !")
    private String email;

    @Size(min = 8, message = "Password must have minimum 8 characters !")
    private String password;

    @Positive(message = "Weight must be positive !")
    private Double weight;

    private String region;

    private String city;

}
