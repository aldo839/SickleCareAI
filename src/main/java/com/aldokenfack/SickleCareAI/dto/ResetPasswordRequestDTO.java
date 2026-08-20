package com.aldokenfack.SickleCareAI.dto;

import com.aldokenfack.SickleCareAI.validator.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ResetPasswordRequestDTO {

    @NotBlank(message = "Reset token is require !")
    private String token;

    @NotBlank(message = "Password is require !")
    @Size(min = 8, message = "Password must have minimum 8 characters !")
    @ValidPassword(message = "Invalid password. Password must be very long or contain small and capital letters, number and specific character")
    private String password;

}
