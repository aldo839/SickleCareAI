package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.config.SecurityConfig;
import com.aldokenfack.SickleCareAI.dto.PatientRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.PatientResponseDTO;
import com.aldokenfack.SickleCareAI.dto.PatientUpdateDTO;
import com.aldokenfack.SickleCareAI.exception.UserAlreadyExistException;
import com.aldokenfack.SickleCareAI.exception.UserNotFoundException;
import com.aldokenfack.SickleCareAI.model.Patient;
import com.aldokenfack.SickleCareAI.model.Role;
import com.aldokenfack.SickleCareAI.repository.PatientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapperService patientMapperService;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository, PatientMapperService patientMapperService, SecurityConfig securityConfig, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.patientMapperService = patientMapperService;
        this.passwordEncoder = passwordEncoder;
    }


    public PatientResponseDTO registerPatient(PatientRegistrationDTO dto){

        // Existing user verification with username and email

        if (patientRepository.existsByUsername(dto.getUsername())){
            System.out.println("Error : " + dto.getUsername() + " already exist !");
            throw new UserAlreadyExistException("Username already exist !");
        }

        if (patientRepository.existsByEmail(dto.getEmail())){
            System.out.println("Error : " + dto.getEmail() + " already exist !");
            throw new UserAlreadyExistException("Email already exist !");
        }

        // Patient creation after verification
        Patient patient = new Patient();

        patient.setUsername(dto.getUsername());
        patient.setEmail(dto.getEmail());
        patient.setRole(Role.PATIENT);
        patient.setPassword(passwordEncoder.encode(dto.getPassword()));
        patient.setSex(dto.getSex());
        patient.setFirstname(dto.getFirstname());
        patient.setLastname(dto.getLastname());
        patient.setSex(dto.getSex());
        patient.setBirthDate(dto.getBirthDate());
        patient.setBloodType(dto.getBloodType());
        patient.setGenotype(dto.getGenotype());
        patient.setWeight(dto.getWeight());
        patient.setRegion(dto.getRegion());
        patient.setCity(dto.getCity());

        Patient savedPatient = patientRepository.save(patient);

        return patientMapperService.mapToResponseDTO(savedPatient);
    }


    public List<PatientResponseDTO> getAllPatients(){

        return patientRepository.findAll().stream()
                .map(patientMapperService::mapToResponseDTO)
                .toList();

    }


    public PatientResponseDTO getPatientById(Long id){

        return patientRepository.findById(id)
                .map(patientMapperService::mapToResponseDTO)
                .orElseThrow(() -> new UserNotFoundException("Patient not found !"));
    }


    public PatientResponseDTO updatePatient(Long id, PatientUpdateDTO dto){

        Optional<Patient> patientToUpdate = patientRepository.findById(id);

        Patient updatedPatient = null;

        if (patientToUpdate.isPresent()){

            Patient patient = patientToUpdate.get();

            patient.setUsername(dto.getUsername());
            patient.setEmail(dto.getEmail());
            patient.setPassword(dto.getEmail());
            patient.setWeight(dto.getWeight());
            patient.setRegion(dto.getRegion());
            patient.setCity(dto.getCity());

            updatedPatient = patientRepository.save(patient);

        } else {
            throw new UserNotFoundException("Patient not found !");
        }

        return patientMapperService.mapToResponseDTO(updatedPatient);
    }


    public void deletePatient(Long id){

        Optional<Patient> patientToDelete = patientRepository.findById(id);

        if (patientToDelete.isPresent()){
            patientRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("Patient Not found !");
        }
    }

}
