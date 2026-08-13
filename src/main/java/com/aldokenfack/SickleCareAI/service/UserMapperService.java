package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.config.JwtUtils;
import com.aldokenfack.SickleCareAI.dto.UserLoginResponseDTO;
import com.aldokenfack.SickleCareAI.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter @Setter
@RequiredArgsConstructor
public class UserMapperService {

    private final JwtUtils jwtUtils;

    public UserLoginResponseDTO mapToResponseDTO(User user){

        String token = jwtUtils.generateToken(user.getUsername());

        return new UserLoginResponseDTO(

                token,
                user.getUsername(),
                user.getRole()

        );
    }
}
