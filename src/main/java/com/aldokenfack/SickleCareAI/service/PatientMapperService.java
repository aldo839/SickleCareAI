package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.dto.PatientResponseDTO;
import com.aldokenfack.SickleCareAI.model.Patient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class PatientMapperService {

    public PatientResponseDTO mapToResponseDTO(Patient patient){

        int age = 0;
        if (patient.getBirthdate() != null){
            age = Period.between(patient.getBirthdate(), LocalDate.now()).getYears();
        }

        return new PatientResponseDTO(

                patient.getId(),
                patient.getUsername(),
                patient.getEmail(),
                patient.getRole(),
                patient.getFirstname() + " " + patient.getLastname(),
                patient.getSex(),
                age,
                patient.getGenotype(),
                patient.getBloodtype(),
                patient.getWeight(),
                patient.getRegion(),
                patient.getCity(),
                patient.getDoctor().getId()

        );

    }

}
