package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.dto.UserResponseDTO;
import com.aldokenfack.SickleCareAI.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter @Setter
public class UserMapperService {

    public UserResponseDTO mapToResponseDTO(User user){

        return new UserResponseDTO(

                user.getUsername(),
                user.getRole()

        );
    }
}
