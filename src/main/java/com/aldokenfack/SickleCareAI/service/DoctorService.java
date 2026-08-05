package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.dto.DoctorRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.DoctorResponseDTO;
import com.aldokenfack.SickleCareAI.dto.DoctorUpdateDTO;
import com.aldokenfack.SickleCareAI.exception.UserAlreadyExistException;
import com.aldokenfack.SickleCareAI.exception.UserNotFoundException;
import com.aldokenfack.SickleCareAI.model.Doctor;
import com.aldokenfack.SickleCareAI.model.Role;
import com.aldokenfack.SickleCareAI.model.Validation;
import com.aldokenfack.SickleCareAI.repository.DoctorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapperService doctorMapperService;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;

    public DoctorResponseDTO registerDoctor(DoctorRegistrationDTO dto){

        // Existing user verification
        if (doctorRepository.existsByUsername(dto.getUsername())){
            System.err.println("Error : " + dto.getUsername() + " already exist !");
            throw new UserAlreadyExistException("This username already exist !");
        }

        if (doctorRepository.existsByEmail(dto.getEmail())){
            System.err.println("Error : " + dto.getEmail() + " already exist !");
            throw new UserAlreadyExistException("This email already exist !");
        }

        // Patient creation after verification
        Doctor doctor = new Doctor();

        doctor.setUsername(dto.getUsername());
        doctor.setEmail(dto.getEmail());
        doctor.setPassword(passwordEncoder.encode(dto.getPassword()));
        doctor.setFirstname(dto.getFirstname());
        doctor.setLastname(dto.getLastname());
        doctor.setRole(Role.ROLE_DOCTOR);
        doctor.setSpeciality(dto.getSpeciality());
        doctor.setMatricule(dto.getMatricule());
        doctor.setValidationLetterReference(dto.getValidationLetterReference());
        doctor.setRegion(dto.getRegion());
        doctor.setCity(dto.getCity());
        doctor.setHospital(dto.getHospital());
        doctor.setHospitalUnit(dto.getHospitalUnit());

        Doctor savedDoctor = doctorRepository.save(doctor);

        validationService.registerUser(savedDoctor);

        return doctorMapperService.mapToResponseDTO(savedDoctor);

    }


    public String activation(Map<String, String> ativation){

        Validation validation = validationService.readCode(ativation.get("code"));

        if (Instant.now().isAfter(validation.getExpiration())){
            throw new RuntimeException("Your code is expire");
        }

        Doctor doctor = doctorRepository.findById(validation.getUser().getId()).orElseThrow(() -> new UserNotFoundException("User not found"));

        doctor.setActivated(true);
        doctorRepository.save(doctor);

        return  doctor.getActivated().toString();
    }


    public List<DoctorResponseDTO> getAllDoctor(){

        return doctorRepository.findAll().stream()
                .map(doctorMapperService::mapToResponseDTO)
                .toList();
    }


    public DoctorResponseDTO getDoctorById(Long id){

        return doctorRepository.findById(id)
                .map(doctorMapperService::mapToResponseDTO)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found !"));
    }


    public DoctorResponseDTO updateDoctor(Long id, DoctorUpdateDTO dto){

        Doctor updatedDoctor = null;

        Optional<Doctor> doctorToUpdate = doctorRepository.findById(id);

        if (doctorToUpdate.isPresent()){

            Doctor doctor = doctorToUpdate.get();

            doctor.setUsername(dto.getUsername());
            doctor.setEmail(dto.getEmail());
            doctor.setPassword(passwordEncoder.encode(dto.getPassword()));
            doctor.setHospitalUnit(dto.getHospitalUnit());
            doctor.setRegion(dto.getRegion());
            doctor.setCity(dto.getCity());
            doctor.setHospital(dto.getHospital());

            updatedDoctor = doctorRepository.save(doctor);

        } else {
            throw new UserNotFoundException("Doctor not found !");
        }

        return doctorMapperService.mapToResponseDTO(updatedDoctor);
    }


    public void deleteDoctor(Long id){

        Optional<Doctor> doctorToDelete = doctorRepository.findById(id);

        if (doctorToDelete.isPresent()){
            doctorRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("Doctor not found !");
        }
    }

}
