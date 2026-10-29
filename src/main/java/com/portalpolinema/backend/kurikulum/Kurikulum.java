package com.portalpolinema.backend.kurikulum;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "kurikulum")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Kurikulum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 25, unique = true)
    private String kode;

    @Column(nullable = false)
    private Integer tahun_berlaku;

    public enum Status {
        Aktif, NonAktif
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "program_studi_id", nullable = false)
    private com.portalpolinema.backend.prodi.Prodi prodi;
}
