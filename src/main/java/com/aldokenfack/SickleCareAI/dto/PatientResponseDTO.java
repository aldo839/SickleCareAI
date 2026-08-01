package com.aldokenfack.SickleCareAI.dto;

import com.aldokenfack.SickleCareAI.model.Bloodtype;
import com.aldokenfack.SickleCareAI.model.Genotype;
import com.aldokenfack.SickleCareAI.model.Role;
import com.aldokenfack.SickleCareAI.model.Sex;

public record PatientResponseDTO(

        Long id,

        String username,

        String email,

        Role role,

        String name,

        Sex sex,

        Integer age,

        Genotype genotype,

        Bloodtype bloodtype,

        Double weight,

        String region,

        String city

) {
}