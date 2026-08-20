package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.dto.ForgotPasswordRequestDTO;
import com.aldokenfack.SickleCareAI.dto.ResetPasswordRequestDTO;
import com.aldokenfack.SickleCareAI.exception.UserNotFoundException;
import com.aldokenfack.SickleCareAI.model.PasswordResetToken;
import com.aldokenfack.SickleCareAI.model.User;
import com.aldokenfack.SickleCareAI.repository.PasswordResetTokenRepository;
import com.aldokenfack.SickleCareAI.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final NotificationService notificationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public void processForgotPassword(ForgotPasswordRequestDTO dto){

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);

        passwordResetTokenRepository.save(passwordResetToken);

        notificationService.sendResetPasswordMessage(passwordResetToken);
    }

    public void updatePassword(ResetPasswordRequestDTO dto){

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (passwordResetToken.isExpired()){
            passwordResetTokenRepository.findByToken(dto.getToken());
            throw new RuntimeException("Token has expired");
        }

        User user = passwordResetToken.getUser();
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        passwordResetTokenRepository.delete(passwordResetToken);
    }

}
