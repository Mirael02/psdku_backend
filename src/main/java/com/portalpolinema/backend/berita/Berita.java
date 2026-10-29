package com.portalpolinema.backend.berita;

import lombok.*;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "berita")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Berita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_berita;

    @Column(nullable = false, length = 100, unique = true)
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

    @Column(nullable = false, length = 100, unique = true)
    private String file;

    @Column(nullable = false, length = 100, unique = true)
    private String slug;

    @Column(nullable = false)
    private int view;

}
