package com.aldokenfack.SickleCareAI.controller;

import com.aldokenfack.SickleCareAI.dto.PatientRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.PatientResponseDTO;
import com.aldokenfack.SickleCareAI.dto.PatientUpdateDTO;
import com.aldokenfack.SickleCareAI.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id){

        return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.OK);
    }


    @PostMapping("/register-patient")
    public ResponseEntity<PatientResponseDTO> registerPatient(@Valid @RequestBody PatientRegistrationDTO dto) {

        return new ResponseEntity<>(patientService.registerPatient(dto), HttpStatus.CREATED);
    }


    @PostMapping("/activate-patient")
    public ResponseEntity<String> activatePatient(@RequestBody Map<String, String> activation) {

        String status = patientService.activation(activation);

        return ResponseEntity.ok("Account successfully activated. \nStatus : " + status);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponseDTO> updatePatient(@Valid @PathVariable Long id, @RequestBody PatientUpdateDTO dto) throws AccessDeniedException {

        return new ResponseEntity<>(patientService.updatePatient(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMIN')")
    public void delete(@PathVariable Long id){

        patientService.deletePatient(id);
    }

    @PutMapping("/select-doctor/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponseDTO> selectDoctor(@PathVariable Long id, @RequestBody Long doctorId) throws AccessDeniedException{
        // PatientId is "id"
        return new ResponseEntity<>(patientService.selectDoctor(id, doctorId), HttpStatus.OK);
    }

    @PutMapping("/validate-patient/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMIN')")
    public ResponseEntity<PatientResponseDTO> validateDoctor(@PathVariable Long id){

        return new ResponseEntity<>(patientService.validatePatient(id), HttpStatus.OK);
    }

}
