package com.aldokenfack.SickleCareAI.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter @Setter
public class DoctorUpdateDTO {

    @NotBlank
    @Size(min = 4, max = 20, message = "Username must contain between 4 and 20 characters !")
    private String username;

    @NotBlank
    @Email(message = "Email must be valid !")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password most have minimum 8 characters !")
    private String password;

    private String hospitalUnit;

    private String region;

    private String city;

    private String hospital;

}
