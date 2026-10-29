package com.portalpolinema.backend.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "SMALLINT UNSIGNED")
    private int id_user;

    @Column(nullable = false, length = 100)
    private String nama;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 150)
    private String password;

    public enum Role {
        super_admin, admin_prodi, admin_kemahasiswaan, admin_manajerial
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(nullable = false, length = 255)
    private String foto;

}
