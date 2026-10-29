package com.portalpolinema.backend.mata_kuliah.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class MataKuliahDto {
    private int id;
    private String kode;
    private String nama;
    private Integer sks_teori;
    private Integer sks_praktikum;
    private Integer jam;
    private String jenis;
    private Integer id_kurikulum;
    private String kode_kurikulum;
    private Integer id_prodi;
}
