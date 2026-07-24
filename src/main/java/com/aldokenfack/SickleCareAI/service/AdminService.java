package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.dto.AdminRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.AdminResponseDTO;
import com.aldokenfack.SickleCareAI.dto.AdminUpdateDTO;
import com.aldokenfack.SickleCareAI.exception.UserAlreadyExistException;
import com.aldokenfack.SickleCareAI.exception.UserNotFoundException;
import com.aldokenfack.SickleCareAI.model.Admin;
import com.aldokenfack.SickleCareAI.model.Role;
import com.aldokenfack.SickleCareAI.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapperService adminMapperService;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, AdminMapperService adminMapperService, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.adminMapperService = adminMapperService;
        this.passwordEncoder = passwordEncoder;
    }


    public AdminResponseDTO registerAdmin(AdminRegistrationDTO dto){

        // Existing account verification
        if (adminRepository.existsByUsername(dto.getUsername())){
            System.err.println("Error : " + dto.getUsername() + " already exist !");
            throw new UserAlreadyExistException("This username already exist !");
        }

        if (adminRepository.existsByEmail(dto.getEmail())){
            System.out.println("Error : " + dto.getEmail() + " already exist !");
            throw new UserAlreadyExistException("This username already exist !");
        }

        // Account creation after verification
        Admin admin = new Admin();

        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setRole(Role.ROLE_ADMIN);
        admin.setFirstname(dto.getFirstname());
        admin.setLastname(dto.getLastname());

        Admin savedAdmin = adminRepository.save(admin);

        return adminMapperService.mapToResponseDTO(savedAdmin);
    }


    public List<AdminResponseDTO> getAllAdmin(){

        return adminRepository.findAll().stream()
                .map(adminMapperService::mapToResponseDTO)
                .toList();
    }


    public AdminResponseDTO getAdminById(Long id){

        return adminRepository.findById(id)
                .map(adminMapperService::mapToResponseDTO)
                .orElseThrow(() -> new UserNotFoundException("Admin mot found !"));
    }


    public AdminResponseDTO updateAdmin(Long id, AdminUpdateDTO dto){

        Admin updatedAdmin = null;

        Optional<Admin> adminToUpdate = adminRepository.findById(id);

        if (adminToUpdate.isPresent()){

            Admin admin = adminToUpdate.get();

            admin.setUsername(dto.getUsername());
            admin.setEmail(dto.getEmail());
            admin.setPassword(passwordEncoder.encode(dto.getPassword()));

            updatedAdmin = adminRepository.save(admin);
        } else {
            throw new UserNotFoundException("Admin not found !");
        }

        return adminMapperService.mapToResponseDTO(updatedAdmin);
    }


    public void deleteAdmin(Long id){

        Optional<Admin> adminToDelete = adminRepository.findById(id);

        if (adminToDelete.isPresent()){
            adminRepository.deleteById(id);
        }
        throw new UserNotFoundException("Admin not found !");
    }

}
