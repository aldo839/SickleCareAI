package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.dto.DoctorResponseDTO;
import com.aldokenfack.SickleCareAI.model.Doctor;
import org.springframework.stereotype.Service;

@Service
public class DoctorMapperService {

    public DoctorResponseDTO mapToResponseDTO(Doctor doctor){

        return new DoctorResponseDTO(

                doctor.getUsername(),
                doctor.getEmail(),
                doctor.getFirstname(),
                doctor.getLastname(),
                doctor.getSpeciality(),
                doctor.getMatricule(),
                doctor.getValidationLetterReference(),
                doctor.getRegion(),
                doctor.getCity(),
                doctor.getHospital(),
                doctor.getHospitalUnit()
        );

    }

}
