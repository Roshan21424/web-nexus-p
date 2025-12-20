package com.personal.nexus.config;

import com.personal.nexus.entity.Admin;
import com.personal.nexus.entity.User;
import com.personal.nexus.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements ApplicationRunner {
    
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {

            if (adminRepository.findByEmail("admin@school.com").isEmpty()) {
                Admin admin = new Admin();
                admin.setName("admin");
                admin.setEmail("admin@school.com");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(User.Role.ADMIN);
                admin.setActive(true);

                adminRepository.save(admin);
                System.out.println("admin created!");

            }
        }
    }
