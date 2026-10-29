package com.portalpolinema.backend.dosen;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dosen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Dosen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nama;

    @Column(nullable = false, length = 100)
    private String nip;

    @Column(nullable = false, length = 100)
    private String jabatan_fungsional;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "id_prodi", nullable = false)
    private com.portalpolinema.backend.prodi.Prodi prodi;
}
