package io.febr.api.config;

import io.febr.api.domain.Role;
import io.febr.api.domain.User;
import io.febr.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class DataInitialization implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitialization.class);
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Check if admin user exists
        if (userRepository.findByEmail("admin@febr.io").isEmpty()) {
            logger.info("Admin user not found, creating...");
            User adminUser = new User();
            adminUser.setEmail("admin@febr.io");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setRole(Role.ROLE_ADMIN);
            adminUser.setFirstName("Admin");
            adminUser.setLastName("Admin");

            userRepository.save(adminUser);
        }
    }
}
