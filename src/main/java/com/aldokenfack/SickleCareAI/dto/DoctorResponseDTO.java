package com.aldokenfack.SickleCareAI.dto;

public record DoctorResponseDTO(

        Long id,

        String username,

        String email,

        String firstname,

        String lastname,

        String speciality,

        String matricule,

        String validationLetterReference,

        String region,

        String city,

        String hospital,

        String hospitalUnit

) {
}
