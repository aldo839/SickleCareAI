package com.aldokenfack.SickleCareAI.controller;

import com.aldokenfack.SickleCareAI.dto.DoctorRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.DoctorResponseDTO;
import com.aldokenfack.SickleCareAI.dto.DoctorUpdateDTO;
import com.aldokenfack.SickleCareAI.service.DoctorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Doctors", description = "Operations on doctors accounts")
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
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable UUID doctorId){

        return new ResponseEntity<>(doctorService.getDoctorById(doctorId), HttpStatus.OK);
    }


    @PostMapping("/register-doctor")
    public ResponseEntity<DoctorResponseDTO> registerDoctor(@Valid @RequestBody DoctorRegistrationDTO dto){

        return new ResponseEntity<>(doctorService.registerDoctor(dto), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@Valid @PathVariable UUID doctorId, @RequestBody DoctorUpdateDTO dto) {

        return new ResponseEntity<>(doctorService.updateDoctor(doctorId, dto), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable UUID doctorId) {

        doctorService.deleteDoctor(doctorId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/validate-doctor/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMIN')")
    public ResponseEntity<DoctorResponseDTO> validateDoctor(@PathVariable UUID doctorId){

        return new ResponseEntity<>(doctorService.validateDoctor(doctorId), HttpStatus.OK);
    }

}
