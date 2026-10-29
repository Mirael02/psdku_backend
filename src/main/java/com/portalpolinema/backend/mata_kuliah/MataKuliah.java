package com.portalpolinema.backend.mata_kuliah;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mata_kuliah")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MataKuliah {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 25, unique = true)
    private String kode;

    @Column(nullable = false, length = 100)
    private String nama;

    @Column(name = "sks_teori", nullable = false)
    private Integer sks_teori;

    @Column(name = "sks_praktikum", nullable = false)
    private Integer sks_praktikum;

    @Column(name = "jam", nullable = false)
    private Integer jam;

    public enum Jenis {
        Wajib, Pilihan
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Jenis jenis;

    @ManyToOne
    @JoinColumn(name = "id_kurikulum", nullable = false)
    private com.portalpolinema.backend.kurikulum.Kurikulum kurikulum;
}
