package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.config.JwtUtils;
import com.aldokenfack.SickleCareAI.dto.PatientRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.PatientResponseDTO;
import com.aldokenfack.SickleCareAI.dto.PatientUpdateDTO;
import com.aldokenfack.SickleCareAI.exception.AccountNotValidatedException;
import com.aldokenfack.SickleCareAI.exception.UserAlreadyExistException;
import com.aldokenfack.SickleCareAI.exception.UserNotFoundException;
import com.aldokenfack.SickleCareAI.model.Doctor;
import com.aldokenfack.SickleCareAI.model.Patient;
import com.aldokenfack.SickleCareAI.model.Role;
import com.aldokenfack.SickleCareAI.model.Validation;
import com.aldokenfack.SickleCareAI.repository.DoctorRepository;
import com.aldokenfack.SickleCareAI.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapperService patientMapperService;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final JwtUtils jwtUtils;
    private final DoctorRepository doctorRepository;
    private final NotificationService notificationService;
    private final AuthService authService;


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
        patient.setRole(Role.ROLE_PATIENT);
        patient.setPassword(passwordEncoder.encode(dto.getPassword()));
        patient.setSex(dto.getSex());
        patient.setFirstname(dto.getFirstname());
        patient.setLastname(dto.getLastname());
        patient.setSex(dto.getSex());
        patient.setBirthdate(dto.getBirthdate());
        patient.setGenotype(dto.getGenotype());
        patient.setBloodtype(dto.getBloodtype());
        patient.setWeight(dto.getWeight());
        patient.setRegion(dto.getRegion());
        patient.setCity(dto.getCity());

        Patient savedPatient = patientRepository.save(patient);

        validationService.registerUser(savedPatient);

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


    public PatientResponseDTO updatePatient(Long id, PatientUpdateDTO dto) throws AccessDeniedException {

        // Fetch the id of the user who is authenticated
        Long currentUserId = authService.getCurrentUser().getId();

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!patient.getId().equals(currentUserId)){
            throw new AccessDeniedException("You cannot update this user");
        }

        // Condition to update only the authorized field if isn't null
        if (dto.getUsername() != null && !dto.getUsername().isBlank()){
            patient.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()){
            patient.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()){
            patient.setPassword(passwordEncoder.encode(dto.getEmail()));
        }
        if (dto.getWeight() != null && dto.getWeight() > 0){
            patient.setWeight(dto.getWeight());
        }
        if (dto.getRegion() != null && !dto.getRegion().isBlank()){
            patient.setRegion(dto.getRegion());
        }
        if (dto.getCity() != null && !dto.getCity().isBlank()){
            patient.setCity(dto.getCity());
        }

        Patient updatedPatient = patientRepository.save(patient);

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


    public String activation(Map<String, String> activation){

        Validation validation = validationService.readCode(activation.get("code"));

        if (Instant.now().isAfter(validation.getExpiration())){
            throw new RuntimeException("Your code is expire");
        }

        Patient patient = patientRepository.findById(validation.getUser().getId()).orElseThrow(() -> new UserNotFoundException("User not found"));

        patient.setActivated(true);

        patientRepository.save(patient);

        return jwtUtils.generateToken(patient.getEmail());
    }


    public PatientResponseDTO validatePatient(Long id){

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));

        patient.setValidated(true);

        this.patientRepository.save(patient);

        // Send notification for successfully validation by admin
        notificationService.sendAdminValidationSuccess(patient);

        return patientMapperService.mapToResponseDTO(patient);
    }


    public PatientResponseDTO selectDoctor(Long patientId, Long doctorId) throws AccessDeniedException {

        Long currentUserId = authService.getCurrentUser().getId();

        // Verify that patient and doctor exists before applying selection
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found"));

        if (!patientId.equals(currentUserId)){
            throw new AccessDeniedException("You cannot select doctor for this user");
        }

        if (patient.getValidated() != true){
            throw new AccountNotValidatedException("Your account is not yet validate by the administrator.\nPlease make sure to register all your information or contact the support");
        }

        patient.setDoctor(doctor);

        Patient savedPatient = patientRepository.save(patient);

        return patientMapperService.mapToResponseDTO(savedPatient);

    }

}
