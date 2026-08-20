package com.aldokenfack.SickleCareAI.service;

import com.aldokenfack.SickleCareAI.model.PasswordResetToken;
import com.aldokenfack.SickleCareAI.model.Role;
import com.aldokenfack.SickleCareAI.model.User;
import com.aldokenfack.SickleCareAI.model.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
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
            throw new MailSendException("Problem to send a mail");
        }

    }


    public void sendAdminValidationSuccess(User user){

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            String subject = null;
            String text = null;

            if (user.getRole() == Role.ROLE_PATIENT){

                 subject = "SickleCare Patient Account Validation";

                 text = String.format(
                         "Hello %s, \nYour account has been officially validated by our administration. " +
                                 "You can now access all features of SickleCare AI." +
                                 "\n See you on the App to select your doctor !",
                         user.getUsername()
                 );

            } else if (user.getRole() == Role.ROLE_DOCTOR) {

                subject = "SickleCare Doctor Account Validation";

                text = String.format(
                        "Hello %s, \nYour account has been officially validated by our administration. " +
                                "You can now access all features of SickleCare AI." +
                                "\n You can now be selected by patients on the App !",
                        user.getUsername()
                );
            }

            message.setFrom("no-reply@sicklecare.org");
            message.setTo(user.getEmail());
            message.setSubject(subject);
            message.setText(text);

            javaMailSender.send(message);

        } catch (MailException e) {
            throw new MailSendException("Problem to send a mail");
        }
    }


    public void sendResetPasswordMessage(PasswordResetToken passwordResetToken){

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            // Adding of the URL of frontend
            String baseUrl = "http://localhost:4200";
            String resetLink = baseUrl + "/reset-password?token=" + passwordResetToken.getToken();

            String text = String.format(
                    "We received a request to reset the password for %s.\nFollow the link below to set a new password.\n%s",
                    passwordResetToken.getUser().getEmail(),
                    resetLink
            );

            message.setFrom("no-reply@sicklecare.org");
            message.setTo(passwordResetToken.getUser().getEmail());
            message.setSubject("Reset your password");
            message.setText(text);

            javaMailSender.send(message);

        } catch (MailException e) {
            throw new MailSendException("Problem to send a mail");
        }

    }

}
