package com.example.hospital.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseTest implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            jdbcTemplate.execute("SELECT 1");
            System.out.println("✅✅✅ DATABASE CONNECTION SUCCESSFUL!");
        } catch (Exception e) {
            System.out.println("❌❌❌ DATABASE CONNECTION FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}