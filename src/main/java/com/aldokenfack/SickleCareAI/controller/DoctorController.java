package com.aldokenfack.SickleCareAI.controller;

import com.aldokenfack.SickleCareAI.dto.DoctorRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.DoctorResponseDTO;
import com.aldokenfack.SickleCareAI.dto.DoctorUpdateDTO;
import com.aldokenfack.SickleCareAI.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;


    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMIN', 'DOCTOR')")
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctor(){

        return new ResponseEntity<>(doctorService.getAllDoctor(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMIN', 'DOCTOR') or #id == authentication.principal.id")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable Long id){

        return new ResponseEntity<>(doctorService.getDoctorById(id), HttpStatus.OK);
    }


    @PostMapping("/register-doctor")
    public ResponseEntity<DoctorResponseDTO> registerDoctor(@Valid @RequestBody DoctorRegistrationDTO dto){

        return new ResponseEntity<>(doctorService.registerDoctor(dto), HttpStatus.CREATED);
    }


    @PostMapping("/activate-doctor")
    public ResponseEntity<String> activateDoctor(@RequestBody Map<String, String> activation){

        String status = doctorService.activation(activation);

        return ResponseEntity.ok("Account successfully activate. \nStatus : " + status);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@Valid @PathVariable Long id, @RequestBody DoctorUpdateDTO dto) throws AccessDeniedException {

        return new ResponseEntity<>(doctorService.updateDoctor(id, dto), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {

        doctorService.deleteDoctor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/validate-doctor/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMIN')")
    public ResponseEntity<DoctorResponseDTO> validateDoctor(@PathVariable Long id){

        return new ResponseEntity<>(doctorService.validateDoctor(id), HttpStatus.OK);
    }

}
