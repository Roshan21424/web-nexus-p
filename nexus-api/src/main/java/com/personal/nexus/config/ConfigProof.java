package com.personal.nexus.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigProof {

    @Value("${spring.data.mongodb.uri:NOT_FOUND}")
    private String mongoUri;

    @PostConstruct
    public void prove() {
        System.out.println("🔥 MONGO URI FROM CONFIG = " + mongoUri);
    }
}