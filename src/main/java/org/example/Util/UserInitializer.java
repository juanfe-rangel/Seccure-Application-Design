package org.example.Util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;



@Configuration
public class UserInitializer {
    @Bean
    public CommandLineRunner initData(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                String hashed = encoder.encode("Admin2025!");
                repo.save(new User("admin", hashed, "ROLE_ADMIN"));
            }
            if (repo.findByUsername("estudiante").isEmpty()) {
                String hashed = encoder.encode("Eci12345!");
                repo.save(new User("estudiante", hashed, "ROLE_USER"));
            }
        };
    }

}
