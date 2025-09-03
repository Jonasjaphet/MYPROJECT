package com.example.hospital.config;

import com.example.hospital.security.CustomUserDetailsService;
import com.example.hospital.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 🔓 RUHUSU ENDPOINTS ZA PUBLIC BILA TOKEN
                        .requestMatchers(
                                "/auth/**",                  // Login & Register
                                "/v3/api-docs/**",          // Swagger API docs
                                "/swagger-ui.html",         // Swagger UI
                                "/swagger-ui/**",           // Swagger UI resources
                                "/swagger-resources/**",    // Swagger resources
                                "/webjars/**",              // Web jars
                                "/api/availability/**"      // Availability (public)
                        ).permitAll()

                        // 👥 ENDPOINTS ZA PATIENTS
                        .requestMatchers(
                                "/api/appointments/book",
                                "/api/appointments/patient/**",
                                "/api/profile/patient"
                        ).hasAnyRole("PATIENT", "ADMIN")

                        // 🩺 ENDPOINTS ZA DOCTORS
                        .requestMatchers(
                                "/api/appointments/doctor/**",
                                "/api/appointments/**/approve",
                                "/api/appointments/**/reject",
                                "/api/availability",
                                "/api/profile/doctor"
                        ).hasAnyRole("DOCTOR", "ADMIN")

                        // ⚙️ ENDPOINTS ZA ADMIN
                        .requestMatchers(
                                "/api/profile/doctor",
                                "/api/profile/patient"
                        ).hasRole("ADMIN")

                        // 🔐 ENDPOINTS ZOTE ZINGINE ZIHITAJI AUTHENTICATION
                        .anyRequest().authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}