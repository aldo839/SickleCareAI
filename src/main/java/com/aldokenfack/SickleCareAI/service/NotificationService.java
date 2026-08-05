package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.model.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender javaMailSender;

    public void sendActivationMessage(Validation validation){

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("no-reply@sicklecare.org");
            message.setTo(validation.getUser().getEmail());
            message.setSubject("Activation Code");

            String text = String.format(
                    "Hello %s, \nYour activation code is %s. \nSee you on the app",
                    validation.getUser().getUsername(),
                    validation.getCode()
            );

            message.setText(text);

            javaMailSender.send(message);

        } catch (MailException e) {
            System.err.println("Error : " + e.getMessage());
            System.out.println("DEBUG : " + validation.getCode());
        }

    }

}
