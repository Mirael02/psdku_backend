package com.portalpolinema.backend.prodi;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import com.portalpolinema.backend.kurikulum.dto.KurikulumDto;
import com.portalpolinema.backend.prodi.dto.ProdiNamaDto;

@RestController
@RequestMapping("/api/prodi")
@RequiredArgsConstructor
public class ProdiController {
    private final ProdiService ps;

    // get all
    @GetMapping
    public List<Prodi> getAll() {
        return ps.getAll();
    }

    // get all
    @GetMapping("/dto")
    public List<ProdiNamaDto> getDto() {
        return ps.getDto();
    }

    // get all prodi
    @GetMapping("/approvalRequest")
    public List<ApprovalRequestList> getAllProdi(@RequestParam(required = false) String search) {
        return ps.getAllProdi(search);
    }

}
