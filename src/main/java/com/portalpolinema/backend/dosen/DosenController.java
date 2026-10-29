package com.portalpolinema.backend.dosen;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import com.portalpolinema.backend.dosen.dto.DosenDto;
import com.portalpolinema.backend.kurikulum.KurikulumService;
import com.portalpolinema.backend.prodi.dto.ProdiNamaDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dosen")
@RequiredArgsConstructor

public class DosenController {
    private final DosenService ds;

    // get all
    @GetMapping
    public List<DosenDto> getAll() {
        return ds.getAll();
    }

    // get all
    @GetMapping("/approvalRequest")
    public List<ApprovalRequestList> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String prodi) {
        return ds.getAll(search, prodi);
    }

}