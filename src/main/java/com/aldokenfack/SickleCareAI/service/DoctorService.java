package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.config.JwtUtils;
import com.aldokenfack.SickleCareAI.dto.DoctorRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.DoctorResponseDTO;
import com.aldokenfack.SickleCareAI.dto.DoctorUpdateDTO;
import com.aldokenfack.SickleCareAI.dto.PatientResponseDTO;
import com.aldokenfack.SickleCareAI.exception.UserAlreadyExistException;
import com.aldokenfack.SickleCareAI.exception.UserNotFoundException;
import com.aldokenfack.SickleCareAI.model.*;
import com.aldokenfack.SickleCareAI.repository.DoctorRepository;
import com.aldokenfack.SickleCareAI.repository.PatientRepository;
import org.springframework.security.access.AccessDeniedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapperService doctorMapperService;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final JwtUtils jwtUtils;
    private final PatientRepository patientRepository;
    private final PatientMapperService patientMapperService;
    private final NotificationService notificationService;
    private final AuthService authService;

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


    public String activation(Map<String, String> activation){

        Validation validation = validationService.readCode(activation.get("code"));

        if (Instant.now().isAfter(validation.getExpiration())){
            throw new RuntimeException("Your code is expire");
        }

        Doctor doctor = doctorRepository.findById(validation.getUser().getId()).orElseThrow(() -> new UserNotFoundException("User not found"));

        doctor.setActivated(true);
        doctorRepository.save(doctor);

        return jwtUtils.generateToken(doctor.getEmail());
    }


    public List<DoctorResponseDTO> getAllDoctor(){

        return doctorRepository.findAll().stream()
                .map(doctorMapperService::mapToResponseDTO)
                .toList();
    }


    public DoctorResponseDTO getDoctorById(UUID doctorId){

        return doctorRepository.findByPublicId(doctorId)
                .map(doctorMapperService::mapToResponseDTO)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found !"));
    }


    public DoctorResponseDTO updateDoctor(UUID doctorId, DoctorUpdateDTO dto) {

        // Fetch the id of the user who is authenticated
        UUID currentUserId = authService.getCurrentUser().getPublicId();

        Doctor doctor = doctorRepository.findByPublicId(doctorId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!doctorId.equals(currentUserId)){
            throw new AccessDeniedException("You cannot update this user");
        }

        // Condition to update only the authorized field if isn't null
        if (dto.getUsername() != null && !dto.getUsername().isBlank()){
            doctor.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()){
            doctor.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()){
            doctor.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getHospital() != null && !dto.getHospital().isBlank()){
            doctor.setHospital(dto.getHospital());
        }
        if (dto.getHospitalUnit() != null && !dto.getHospitalUnit().isBlank()){
            doctor.setHospitalUnit(dto.getHospitalUnit());
        }
        if (dto.getRegion() != null && !dto.getRegion().isBlank()){
            doctor.setRegion(dto.getRegion());
        }
        if (dto.getCity() != null && !dto.getCity().isBlank()){
            doctor.setCity(dto.getCity());
        }

        Doctor updatedDoctor = doctorRepository.save(doctor);

        return doctorMapperService.mapToResponseDTO(updatedDoctor);
    }


    public void deleteDoctor(UUID doctorId){

        UUID currentUserId = authService.getCurrentUser().getPublicId();

        Doctor doctor = doctorRepository.findByPublicId(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found"));

        doctorRepository.delete(doctor);

        log.warn("Doctor {} delete by the user with id : {}", doctor.getUsername(), currentUserId);

    }


    // Validate doctor account by admin
    public DoctorResponseDTO validateDoctor(UUID doctorId){

        UUID currentUserId = authService.getCurrentUser().getPublicId();

        Doctor doctor = doctorRepository.findByPublicId(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found"));

        doctor.setValidated(true);

        this.doctorRepository.save(doctor);

        // Send notification for successfully validation by admin
        notificationService.sendAdminValidationSuccess(doctor);

        log.info("Admin with id {} have activated the user doctor with username : {}", currentUserId, doctor.getUsername());
        return doctorMapperService.mapToResponseDTO(doctor);
    }


    // Fetch all patients of one doctor
    public List<PatientResponseDTO> getPatientByDoctor(UUID doctorId){

        return patientRepository.findByDoctorPublicId(doctorId).stream()
                .map(patientMapperService::mapToResponseDTO)
                .toList();
    }

}
