package com.aldokenfack.SickleCareAI.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DoctorRegistrationDTO {

    @NotBlank(message = "Username is require !")
    @Size(min = 4, max = 20, message = "Username must contain between 4 and 20 characters !")
    private String username;

    @Email(message = "Email must be valid !")
    @NotBlank(message = "Email is require !")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password most have minimum 8 characters !")
    private String password;

    private String firstname;

    private String lastname;

    private String speciality;

    private String matricule;

    private String validationLetterReference;

    private String hospitalUnit;

    private String region;

    private String city;

    private String hospital;

}
