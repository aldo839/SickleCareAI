package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.dto.AdminResponseDTO;
import com.aldokenfack.SickleCareAI.model.Admin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter @Setter
public class AdminMapperService {

    public AdminResponseDTO mapToResponseDTO(Admin admin){

        return new AdminResponseDTO(

                admin.getUsername(),
                admin.getFirstname(),
                admin.getLastname()

        );
    }

}
