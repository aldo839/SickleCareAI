package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.exception.UserNotFoundException;
import com.aldokenfack.SickleCareAI.model.User;
import com.aldokenfack.SickleCareAI.model.Validation;
import com.aldokenfack.SickleCareAI.repository.ValidationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final ValidationRepository validationRepository;
    private final NotificationService notificationService;

    public void registerUser(User user){

        Validation validation = new Validation();

        Instant creation = Instant.now();
        Instant expiration = creation.plus(10, ChronoUnit.MINUTES);

        SecureRandom random = new SecureRandom();
        Integer randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);

        validation.setUser(user);
        validation.setCreation(creation);
        validation.setExpiration(expiration);
        validation.setCode(code);

        validationRepository.save(validation);

        notificationService.sendActivationMessage(validation);

    }


    public Validation readCode(String code){

        return validationRepository.findByCode(code).orElseThrow(() -> new UserNotFoundException("Error : code not found"));
    }

    public void deleteValidation(Validation validation){

        validationRepository.delete(validation);
    }

}
