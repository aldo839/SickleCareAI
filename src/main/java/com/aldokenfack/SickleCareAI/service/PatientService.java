package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.dto.PatientRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.PatientResponseDTO;
import com.aldokenfack.SickleCareAI.dto.PatientUpdateDTO;
import com.aldokenfack.SickleCareAI.exception.AccountNotValidatedException;
import com.aldokenfack.SickleCareAI.exception.UserAlreadyExistException;
import com.aldokenfack.SickleCareAI.exception.UserNotFoundException;
import com.aldokenfack.SickleCareAI.model.Doctor;
import com.aldokenfack.SickleCareAI.model.Patient;
import com.aldokenfack.SickleCareAI.model.Role;
import com.aldokenfack.SickleCareAI.repository.DoctorRepository;
import com.aldokenfack.SickleCareAI.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapperService patientMapperService;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
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


    public PatientResponseDTO getPatientById(UUID patientId){

        return patientRepository.findByPublicId(patientId)
                .map(patientMapperService::mapToResponseDTO)
                .orElseThrow(() -> new UserNotFoundException("Patient not found !"));
    }


    public PatientResponseDTO updatePatient(UUID patientId, PatientUpdateDTO dto) throws AccessDeniedException {

        // Fetch the id of the user who is authenticated
        UUID currentUserId = authService.getCurrentUser().getPublicId();

        Patient patient = patientRepository.findByPublicId(patientId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!patientId.equals(currentUserId)){
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


    public void deletePatient(UUID patientId){

        UUID currentUserId = authService.getCurrentUser().getPublicId();

        Patient patient = patientRepository.findByPublicId(patientId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        patientRepository.delete(patient);

        log.warn("Patient {} delete by the user with id : {}", patient.getUsername(), currentUserId);
    }


    public PatientResponseDTO validatePatient(UUID patientId){

        Patient patient = patientRepository.findByPublicId(patientId)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));

        patient.setValidated(true);

        this.patientRepository.save(patient);

        // Send notification for successfully validation by admin
        notificationService.sendAdminValidationSuccess(patient);

        return patientMapperService.mapToResponseDTO(patient);
    }


    public PatientResponseDTO selectDoctor(UUID patientId, UUID doctorId) {

        UUID currentUserId = authService.getCurrentUser().getPublicId();

        // Verify that patient and doctor exists before applying selection
        Patient patient = patientRepository.findByPublicId(patientId)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findByPublicId(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found"));

        if (!patientId.equals(currentUserId)){
            throw new AccessDeniedException("You cannot select doctor for this user");
        }

        if (patient.getValidated() != true){
            throw new AccountNotValidatedException("Your account is not yet validate by the administrator.\nPlease make sure to register all your information or contact the support");
        }

        patient.setDoctor(doctor);

        Patient savedPatient = patientRepository.save(patient);

        log.info("Patient '{}' have choose the doctor with username '{}'", patient.getUsername(), doctor.getUsername());
        return patientMapperService.mapToResponseDTO(savedPatient);

    }

}
