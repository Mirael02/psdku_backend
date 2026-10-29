package com.portalpolinema.backend.pengumuman;

import lombok.*;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "pengumuman")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Pengumuman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_pengumuman;

    @Column(nullable = false, length = 100)
    private String judul;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String deskripsi;

    public enum Kategori {
        Akademik, Kemahasiswaan, Umum
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Kategori kategori;

    @Column(nullable = false)
    private LocalDate tanggal;

    @Column(nullable = false, length = 255)
    private String file;

    @Column(nullable = false)
    private int view;

}
