package com.orchestrator.controlplane.config; // Apna package name confirm kar lena

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Explicit CORS configuration attach kar rahe hain
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CSRF disable kar rahe hain taaki POST requests block na hon
                .csrf(csrf -> csrf.disable())
                // Sab kuch allow kar rahe hain abhi ke liye
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Yahan tumhara Vercel ka URL aur local ka URL allow kar diya hai
        configuration.setAllowedOrigins(Arrays.asList(
                "https://control-plane-ui-one.vercel.app",
                "http://localhost:5173"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}