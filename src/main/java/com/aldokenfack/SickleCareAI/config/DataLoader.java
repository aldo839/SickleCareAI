package com.aldokenfack.SickleCareAI.config;

import com.aldokenfack.SickleCareAI.model.Role;
import com.aldokenfack.SickleCareAI.model.User;
import com.aldokenfack.SickleCareAI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    @Value("${sicklecareai.root.username}")
    private String username;

    @Value("${sicklecareai.root.email}")
    private String email;

    @Value("${sicklecareai.root.password}")
    private String password;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.existsByRole(Role.ROLE_ROOT)){

            User root = new User();

            root.setUsername(username);
            root.setEmail(email);
            root.setPassword(passwordEncoder.encode(password));
            root.setRole(Role.ROLE_ROOT);

            userRepository.save(root);
            log.info("Root account created with username {}", root.getUsername());
        }

    }
}
