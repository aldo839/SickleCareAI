package com.aldokenfack.SickleCareAI.dto;

import com.aldokenfack.SickleCareAI.model.Bloodtype;
import com.aldokenfack.SickleCareAI.model.Genotype;
import com.aldokenfack.SickleCareAI.model.Sex;
import com.aldokenfack.SickleCareAI.validator.ValidPassword;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class PatientRegistrationDTO {

    @NotBlank(message = "Username is require !")
    @Pattern(regexp = "^[a-zA-Z\\d._-]+$", message = "Username must only contain letters, numbers, dots, or dashes")
    @Size(min = 4, max = 20, message = "Username must have between 4 and 20 characters !")
    private String username;

    @NotBlank(message = "Email is require")
    @Email(message = "Email must be valid !")
    private String email;

    @NotBlank(message = "Password is require !")
    @Size(min = 8, message = "Password must have minimum 8 characters !")
    @ValidPassword(message = "Invalid password. Password must be very long or contain small and capital letters, number and specific character")
    private String password;

    @Size(max = 20, message = "Firstname must have less than 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\d]+$", message = "Firstname must only contain letter")
    private String firstname;

    @Size(max = 20, message = "Lastname must have less than 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\d]+$", message = "Firstname must only contain letter")
    private String lastname;

    private Sex sex;

    @Past(message = "You birth date must be pass")
    private LocalDate birthdate;

    @NotNull(message = "Genotype is mandatory, choose one between AA, AS, SS, AC, SC, CC")
    private Genotype genotype;

    @NotNull(message = "Blood type is mandatory, choose between A+, A-, B+, B-, AB+, AB-, O+, O-")
    private Bloodtype bloodtype;

    @Positive(message = "Weight must be positive !")
    @Max(value = 300, message = "Weight must be less that 300 (Kg)")
    private Double weight;

    @Size(max = 20, message = "Region must be less than 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\d\\s]+$", message = "Region must only contain letter, number and space")
    private String region;

    @Size(max = 20, message = "City must be less than 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\d\\s]+$", message = "Region must only contain letter, number and space")
    private String city;

}