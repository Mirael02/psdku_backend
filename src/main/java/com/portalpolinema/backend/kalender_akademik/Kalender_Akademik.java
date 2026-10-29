package com.portalpolinema.backend.kalender_akademik;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "kalender_akademik")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Kalender_Akademik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_kalender_akademik;

    @Column(nullable = false, length = 10, unique = true)
    private String tahun_akademik;

    @Column(nullable = false, length = 255, unique = true)
    private String file;

    @Column(nullable = false, length = 255, unique = true)
    private String gambar;

    public enum Kondisi {
        AKTIF, ARSIP
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Kondisi kondisi;

}
