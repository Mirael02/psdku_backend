package com.portalpolinema.backend.prodi;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "program_studi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Prodi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nama;

    @Column(nullable = false, length = 25, unique = true)
    private String kode;

    public enum Jenjang {
        D3, D4
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Jenjang jenjang;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
