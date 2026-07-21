package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.dto.DoctorRegistrationDTO;
import com.aldokenfack.SickleCareAI.dto.DoctorResponseDTO;
import com.aldokenfack.SickleCareAI.dto.DoctorUpdateDTO;
import com.aldokenfack.SickleCareAI.model.Doctor;
import com.aldokenfack.SickleCareAI.repository.DoctorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapperService doctorMapperService;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository, DoctorMapperService doctorMapperService, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.doctorMapperService = doctorMapperService;
        this.passwordEncoder = passwordEncoder;
    }


    public DoctorResponseDTO registerDoctor(DoctorRegistrationDTO dto){

        // Existing user verification
        if (doctorRepository.existsByUsername(dto.getUsername())){
            System.err.println("Error : " + dto.getUsername() + " already exist !");
            throw new RuntimeException("This username already exist !");
        }

        if (doctorRepository.existsByEmail(dto.getEmail())){
            System.err.println("Error : " + dto.getEmail() + " already exist !");
            throw new RuntimeException("This email already exist !");
        }

        // Patient creation after verification
        Doctor doctor = new Doctor();

        doctor.setUsername(dto.getUsername());
        doctor.setEmail(dto.getEmail());
        doctor.setPassword(passwordEncoder.encode(dto.getPassword()));
        doctor.setFirstname(dto.getFirstname());
        doctor.setLastname(dto.getLastname());
        doctor.setSpeciality(dto.getSpeciality());
        doctor.setMatricule(dto.getMatricule());
        doctor.setValidationLetterReference(dto.getValidationLetterReference());
        doctor.setHospitalUnit(dto.getHospitalUnit());
        doctor.setRegion(dto.getRegion());
        doctor.setCity(dto.getCity());
        doctor.setHospital(dto.getHospital());

        Doctor savedDoctor = doctorRepository.save(doctor);

        return doctorMapperService.mapToResponseDTO(savedDoctor);

    }


    public List<DoctorResponseDTO> getAllDoctor(){

        return doctorRepository.findAll().stream()
                .map(doctorMapperService::mapToResponseDTO)
                .toList();
    }


    public DoctorResponseDTO getDoctorById(Long id){

        return doctorRepository.findById(id)
                .map(doctorMapperService::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("Doctor not found !"));
    }


    public DoctorResponseDTO updateDoctor(Long id, DoctorUpdateDTO dto){

        Doctor updatedDoctor = null;

        Optional<Doctor> doctorToUpdate = doctorRepository.findById(id);

        if (doctorToUpdate.isPresent()){

            Doctor doctor = doctorToUpdate.get();

            doctor.setUsername(dto.getUsername());
            doctor.setEmail(dto.getEmail());
            doctor.setPassword(passwordEncoder.encode(dto.getPassword()));
            doctor.setHospitalUnit(dto.getHospitalUnit());
            doctor.setRegion(dto.getRegion());
            doctor.setCity(dto.getCity());
            doctor.setHospital(dto.getHospital());

            updatedDoctor = doctorRepository.save(doctor);

        } else {
            throw new RuntimeException("Doctor not found !");
        }

        return doctorMapperService.mapToResponseDTO(updatedDoctor);
    }


    public void deleteDoctor(Long id){

        Optional<Doctor> doctorToDelete = doctorRepository.findById(id);

        if (doctorToDelete.isPresent()){
            doctorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Doctor not found !");
        }
    }

}
