package com.aldokenfack.SickleCareAI.controller;

import com.aldokenfack.SickleCareAI.dto.PatientRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.PatientResponseDTO;
import com.aldokenfack.SickleCareAI.dto.PatientUpdateDTO;
import com.aldokenfack.SickleCareAI.service.PatientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Patients", description = "Operations on patients accounts")
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatient(){

        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMIN', 'DOCTOR') or #id == authentication.principal.id")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable UUID patientId){

        return new ResponseEntity<>(patientService.getPatientById(patientId), HttpStatus.OK);
    }


    @PostMapping("/register-patient")
    public ResponseEntity<PatientResponseDTO> registerPatient(@Valid @RequestBody PatientRegistrationDTO dto) {

        return new ResponseEntity<>(patientService.registerPatient(dto), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponseDTO> updatePatient(@Valid @PathVariable UUID patientId, @RequestBody PatientUpdateDTO dto) {

        return new ResponseEntity<>(patientService.updatePatient(patientId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMIN')")
    public void delete(@PathVariable UUID patientId){

        patientService.deletePatient(patientId);
    }

    @PutMapping("/select-doctor/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponseDTO> selectDoctor(@PathVariable UUID patientId, @RequestBody UUID doctorId) {
        // PatientId is "id"
        return new ResponseEntity<>(patientService.selectDoctor(patientId, doctorId), HttpStatus.OK);
    }

    @PutMapping("/validate-patient/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMIN')")
    public ResponseEntity<PatientResponseDTO> validateDoctor(@PathVariable UUID patientId){

        return new ResponseEntity<>(patientService.validatePatient(patientId), HttpStatus.OK);
    }

}
