package com.portalpolinema.backend.user;

import java.io.IOException;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portalpolinema.backend.user.User.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // get all
    public List<User> list() {
        return userRepository.findAll();
    }

    // get by id
    public User getById(Integer id_user) {
        return userRepository.findById(id_user)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    // create
    public User create(String nama, String email, String password, String role, String foto) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email sudah terdaftar: " + email);
        }

        User us = new User();
        us.setNama(nama);
        us.setEmail(email);
        us.setPassword(passwordEncoder.encode(password));
        us.setRole(Role.valueOf(role));
        us.setFoto(foto != null ? foto : "default.png");
        return userRepository.save(us);
    }

    // update
    public User update(Integer id_user, String email, String password, String role) {
        User us = getById(id_user);

        us.setEmail(email);
        us.setPassword(passwordEncoder.encode(password));
        us.setRole(Role.valueOf(role));
        return userRepository.save(us);
    }

    // delete
    public void delete(Integer id_user) {
        User us = getById(id_user);
        userRepository.delete(us);
    }

}
