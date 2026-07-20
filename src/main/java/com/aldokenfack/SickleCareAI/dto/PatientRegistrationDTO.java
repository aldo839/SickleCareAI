package com.aldokenfack.SickleCareAI.dto;

import com.aldokenfack.SickleCareAI.model.Sex;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class PatientRegistrationDTO {

    @NotBlank(message = "Username is require !")
    @Size(min = 4, max = 20, message = "Username must have minimum 4 characters and maximum 20 !")
    private String username;

    @Email(message = "Email must be valid !")
    @NotBlank
    private String email;

    @NotBlank(message = "Password is require !")
    @Size(min = 8, message = "Password must have minimum 8 characters !")
    private String password;

    private String firstname;
    private String lastname;
    private Sex sex;

    @Past(message = "You birth date must be pass !")
    private LocalDate birthDate;

    private String bloodType;
    private String genotype;

    @Positive(message = "Weight must be positive !")
    private Double weight;

    private String region;
    private String city;

}
