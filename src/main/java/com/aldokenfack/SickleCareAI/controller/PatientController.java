package com.aldokenfack.SickleCareAI.controller;

import com.aldokenfack.SickleCareAI.dto.PatientRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.PatientResponseDTO;
import com.aldokenfack.SickleCareAI.dto.PatientUpdateDTO;
import com.aldokenfack.SickleCareAI.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @PostMapping
    public ResponseEntity<PatientResponseDTO> registerPatient(@RequestBody PatientRegistrationDTO dto) {

        return new ResponseEntity<>(patientService.registerPatient(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable Long id, @RequestBody PatientUpdateDTO dto){

        return new ResponseEntity<>(patientService.updatePatient(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

        patientService.deletePatient(id);
    }

}
