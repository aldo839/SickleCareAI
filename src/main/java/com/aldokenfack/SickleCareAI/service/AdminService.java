package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.dto.AdminRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.AdminResponseDTO;
import com.aldokenfack.SickleCareAI.dto.AdminUpdateDTO;
import com.aldokenfack.SickleCareAI.exception.UserAlreadyExistException;
import com.aldokenfack.SickleCareAI.exception.UserNotFoundException;
import com.aldokenfack.SickleCareAI.model.Admin;
import com.aldokenfack.SickleCareAI.model.Role;
import com.aldokenfack.SickleCareAI.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapperService adminMapperService;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final AuthService authService;

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

        validationService.registerUser(savedAdmin);

        return adminMapperService.mapToResponseDTO(savedAdmin);
    }


    public List<AdminResponseDTO> getAllAdmin(){

        return adminRepository.findAll().stream()
                .map(adminMapperService::mapToResponseDTO)
                .toList();
    }


    public AdminResponseDTO getAdminById(UUID publicId){

        return adminRepository.findByPublicId(publicId)
                .map(adminMapperService::mapToResponseDTO)
                .orElseThrow(() -> new UserNotFoundException("Admin mot found !"));
    }


    public AdminResponseDTO updateAdmin(UUID publicId, AdminUpdateDTO dto){

        UUID currentUserId = authService.getCurrentUser().getPublicId();

        Admin admin = adminRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!publicId.equals(currentUserId)){
            throw new AccessDeniedException("You cannot update this user");
        }

        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));

        Admin updatedAdmin = adminRepository.save(admin);

        return adminMapperService.mapToResponseDTO(updatedAdmin);
    }


    public void deleteAdmin(UUID publicId){

        Admin adminToDelete = adminRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        this.adminRepository.deleteByPublicId(publicId);
    }

}
