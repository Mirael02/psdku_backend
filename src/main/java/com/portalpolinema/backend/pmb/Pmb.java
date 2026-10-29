// Backend/src/main/java/com/portalpolinema/backend/pmb/Pmb.java
package com.portalpolinema.backend.pmb;

import com.portalpolinema.backend.previewimage.PreviewImage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pmb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pmb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_pmb;

    public enum JalurPendaftaran {
        SNBP, SNBT, Mandiri
    }

    public enum Kondisi {
        AKTIF, ARSIP
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private JalurPendaftaran jalur_pendaftaran;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Kondisi kondisi;

    @Column(nullable = true)
    private LocalDate tanggal_mulai;

    @Column(nullable = true)
    private LocalDate tanggal_selesai;

    @Column(nullable = true, length = 255)
    private String file;

    @Column(nullable = true, length = 15)
    private String nama_brosur;

    @OneToMany(mappedBy = "pmb", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("page_no ASC")
    private List<PreviewImage> previewImages = new ArrayList<>();
}
