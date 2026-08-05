package com.aldokenfack.SickleCareAI.controller;

import com.aldokenfack.SickleCareAI.dto.PatientRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.PatientResponseDTO;
import com.aldokenfack.SickleCareAI.dto.PatientUpdateDTO;
import com.aldokenfack.SickleCareAI.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatient(){

        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
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
    public ResponseEntity<PatientResponseDTO> updatePatient(@Valid @PathVariable Long id, @RequestBody PatientUpdateDTO dto){

        return new ResponseEntity<>(patientService.updatePatient(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

        patientService.deletePatient(id);
    }

}
