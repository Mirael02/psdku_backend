package com.portalpolinema.backend.kurikulum.dto;

import com.portalpolinema.backend.kurikulum.Kurikulum;
import com.portalpolinema.backend.prodi.dto.ProdiNamaDto;

import lombok.*;

@Getter
@AllArgsConstructor
public class KurikulumDto {
    private int id;
    private String kode;
    private Integer tahun_berlaku;
    private Kurikulum.Status status;
    private ProdiNamaDto prodi;
}