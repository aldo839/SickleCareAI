package com.aldokenfack.SickleCareAI.controller;

import com.aldokenfack.SickleCareAI.dto.AdminRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.AdminResponseDTO;
import com.aldokenfack.SickleCareAI.dto.AdminUpdateDTO;
import com.aldokenfack.SickleCareAI.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ROOT')")
    public ResponseEntity<List<AdminResponseDTO>> getAllAdmin(){

        return new ResponseEntity<>(adminService.getAllAdmin(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROOT') or #id == authentication.principal.id")
    public ResponseEntity<AdminResponseDTO> getAdminById(@PathVariable Long id){

        return new ResponseEntity<>(adminService.getAdminById(id), HttpStatus.OK);
    }


    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ROOT')")
    public ResponseEntity<AdminResponseDTO> registerAdmin(@Valid @RequestBody AdminRegistrationDTO dto){

        return new ResponseEntity<>(adminService.registerAdmin(dto), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROOT) or #id == authentication.principal.id")
    public ResponseEntity<AdminResponseDTO> updateAdmin(@Valid @PathVariable Long id, @RequestBody AdminUpdateDTO dto){

        return new ResponseEntity<>(adminService.updateAdmin(id, dto), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROOT')")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
