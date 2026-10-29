package com.portalpolinema.backend.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.portalpolinema.backend.auth.dto.LoginRequest;
import com.portalpolinema.backend.auth.dto.LoginResponse;
import com.portalpolinema.backend.user.User;
import com.portalpolinema.backend.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;
        private final UserRepository userRepository;

        @Value("${jwt.expiration}")
        private long jwtExpiration;

        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
                Authentication auth = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

                String token = jwtService.generateToken(
                                (org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal());

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

                return ResponseEntity.ok(
                                new LoginResponse(
                                                token,
                                                jwtExpiration,
                                                user.getId_user(),
                                                user.getEmail(),
                                                user.getNama(),
                                                user.getRole().name(),
                                                user.getFoto()));
        }
}
