package com.itassettracker.it_asset_tracker.config;

import com.itassettracker.it_asset_tracker.entity.Role;
import com.itassettracker.it_asset_tracker.entity.User;
import com.itassettracker.it_asset_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner createInitialAdmin(UserRepository users, PasswordEncoder encoder,
            @Value("${APP_ADMIN_USERNAME:}") String username,
            @Value("${APP_ADMIN_PASSWORD:}") String password) {
        return args -> {
            if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
                System.out.println("Initial admin not created: set APP_ADMIN_USERNAME and APP_ADMIN_PASSWORD.");
                return;
            }
            if (!users.existsByUsername(username.trim())) {
                users.save(new User(username.trim(), encoder.encode(password), Role.ROLE_ADMIN));
                System.out.println("Initial administrator account created from environment configuration.");
            }
        };
    }
}
