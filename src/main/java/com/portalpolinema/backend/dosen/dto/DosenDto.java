package com.portalpolinema.backend.dosen.dto;

import com.portalpolinema.backend.prodi.dto.ProdiNamaDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DosenDto {
    private int id;
    private String nama;
    private String nip;
    private String jabatan_fungsional;
    private Boolean isActive;
    private ProdiNamaDto prodi;
}
