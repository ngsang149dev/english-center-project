package com.abuenglishcenter.managementsystem.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component 
public class AdminSeeder implements ApplicationRunner{

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder, @Value("${app.admin.username:}") String adminUsername, @Value("${app.admin.password:}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override 
    @Transactional 
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(Role.ADMIN)) return;

        if (adminUsername.isBlank() || adminPassword.length() < 8) {
            log.warn("No ADMIN exists and APP_ADMIN_USERNAME / APP_ADMIN_PASSWORD "
                    + "are missing or too short. Skipping admin seed.");
            return;
        }

        if (userRepository.existsByUsername(adminUsername)) { 
            log.warn("Username '{}' is already taken by a non-admin user. Skipping admin seed.", adminUsername);
            return;
        }

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setFullName("Administrator");
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
        log.info("Seeded initial ADMIN user '{}'", adminUsername);
    }
}
