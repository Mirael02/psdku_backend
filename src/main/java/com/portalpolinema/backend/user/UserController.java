package com.portalpolinema.backend.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // get all
    @GetMapping
    public List<User> list() {
        return userService.list();
    }

    // get by id
    @GetMapping("/{id_user}")
    public User get(@PathVariable("id_user") Integer id_user) {
        return userService.getById(id_user);
    }

    // create
    @PostMapping
    public ResponseEntity<User> create(
            @RequestParam("nama") String nama,
            @RequestParam("emailAdmin") String email,
            @RequestParam("passwordAdmin") String password,
            @RequestParam("role") String role,
            @RequestParam(value = "foto", required = false) String foto) throws IOException {
        User us = userService.create(nama, email, password, role, foto);
        return ResponseEntity.ok(us);
    }

    @PutMapping("/{id_user}")
    public ResponseEntity<User> update(
            @PathVariable Integer id_user,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role) {

        User us = userService.update(id_user, email, password, role);
        return ResponseEntity.ok(us);
    }

    // delete
    @DeleteMapping("/{id_user}")
    public ResponseEntity<Void> delete(
            @PathVariable("id_user") Integer id_user) {
        userService.delete(id_user);
        return ResponseEntity.noContent().build();
    }

}
