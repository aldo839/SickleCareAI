package com.aldokenfack.SickleCareAI.dto;

import com.aldokenfack.SickleCareAI.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DoctorRegistrationDTO {

    @NotBlank(message = "Username is require !")
    @Pattern(regexp = "^[a-zA-Z\\d]+$", message = "Username must only contain letter and number")
    @Size(min = 4, max = 20, message = "Username must have between 4 and 20 characters !")
    private String username;

    @Email(message = "Email must be valid !")
    @NotBlank(message = "Email is require !")
    private String email;

    @NotBlank(message = "Password is require !")
    @Size(min = 8, message = "Password most have minimum 8 characters !")
    @ValidPassword(message = "Invalid password. Password must be very long or contain small and capital letters, number and specific character !")
    private String password;

    @Size(max = 20, message = "Firstname must have less than 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\d]+$", message = "Firstname must only contain letter")
    private String firstname;

    @Size(max = 20, message = "Lastname must have less than 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\d]+$", message = "Firstname must only contain letter")
    private String lastname;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Speciality can only contain letters and space")
    private String speciality;

    @Pattern(regexp = "^[a-zA-Z\\d\\s]+$", message = "Matricule can only contain letters, numbers and space")
    private String matricule;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "validation letter reference can only contain letters and space")
    private String validationLetterReference;

    @Size(max = 20, message = "Region must be less than 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\d\\s]+$", message = "Region must only contain letter, number and space")
    private String region;

    @Size(max = 20, message = "City must be less than 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\d\\s]+$", message = "City must only contain letter, number and space")
    private String city;

    @Size(max = 50, message = "Hospital must be less than 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\d\\s]+$", message = "Hospital must only contain letter, number and space")
    private String hospital;

    @Size(max = 50, message = "Hospital Unit must be less than 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\d\\s]+$", message = "Hospital unit must only contain letter, number and space")
    private String hospitalUnit;

}
