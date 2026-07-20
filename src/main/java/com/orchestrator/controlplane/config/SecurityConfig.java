// YAHAN APNA CORRECT PACKAGE PATH DAAL DENA
package com.orchestrator.controlplane.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS ko enable karta hai (jo @CrossOrigin tumne Controller mein lagaya hai)
                .cors(cors -> cors.configure(http))
                // CSRF protection ko disable karta hai taaki POST requests block na hon
                .csrf(csrf -> csrf.disable())
                // Abhi ke liye saari API requests bina authentication ke allow kar dega
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}