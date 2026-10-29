package com.portalpolinema.backend.jurnal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jurnal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jurnal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255)
    private String judul;

    @Column(nullable = false, unique = true, length = 255)
    private String slug;

    @Column(length = 255)
    private String gambar;

    @Column(length = 255)
    private String link;
}
