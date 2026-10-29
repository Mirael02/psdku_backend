package com.portalpolinema.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.portalpolinema.backend.auth.CustomUserDetailsService;
import com.portalpolinema.backend.auth.JwtAuthFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/download/**").permitAll()
                        .requestMatchers("/api/uploads/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        
                        // Protect approval request endpoints for all modules FIRST
                        .requestMatchers("/api/*/approvalRequest/**").authenticated()
                        
                        .requestMatchers("/api/prodi/**").permitAll()
                        .requestMatchers("/api/pengumuman/**").permitAll()
                        .requestMatchers("/api/berita/**").permitAll()
                        .requestMatchers("/api/pmb/**").permitAll()
                        .requestMatchers("/api/kalender_akademik/**").permitAll()
                        .requestMatchers("/api/kurikulum/**").permitAll()
                        .requestMatchers("/api/mata-kuliah/**").permitAll()
                        .requestMatchers("/api/dosen/**").permitAll()

                        // ApprovalRequest: akses dibatasi per operasi
                        .requestMatchers(HttpMethod.GET, "/api/approvalRequest/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/approvalRequest").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/approvalRequest/*/approve/*").hasRole("super_admin")
                        .requestMatchers(HttpMethod.POST, "/api/approvalRequest/*/approve-update/*")
                        .hasRole("super_admin")
                        .requestMatchers(HttpMethod.PUT, "/api/approvalRequest/*/reject").hasRole("super_admin")
                        .requestMatchers(HttpMethod.PUT, "/api/approvalRequest/*/update").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/approvalRequest/**").authenticated()
                        // Izinkan POST /api/user tanpa auth untuk setup awal admin
                        // HAPUS baris ini setelah semua admin sudah dibuat!
                        // .requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // temp di BackendApplication
    @Bean
    CommandLineRunner printHash(PasswordEncoder enc) {
        return args -> System.out.println(enc.encode("123"));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
