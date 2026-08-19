package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.dto.UserLoginResponseDTO;
import com.aldokenfack.SickleCareAI.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapperService {

    public UserLoginResponseDTO mapToResponseDTO(User user, String accessToken, String refreshToken){

        return new UserLoginResponseDTO(

                user.getUsername(),
                user.getRole(),
                accessToken,
                refreshToken

        );
    }
}
