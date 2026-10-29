package com.portalpolinema.backend.mata_kuliah;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import com.portalpolinema.backend.mata_kuliah.dto.MataKuliahDto;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/mata-kuliah")
@RequiredArgsConstructor

public class MataKuliahController {
    private final MataKuliahService service;

    // get all for guest
    @GetMapping("/guest")
    public List<MataKuliahDto> getAllGuest() {
        return service.getAll();
    }

    // get by id
    @GetMapping("/{id}")
    public MataKuliahDto getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    // get by kode
    @GetMapping("/kode/{kode}")
    public MataKuliahDto getByKode(@PathVariable String kode) {
        return service.getByKode(kode);
    }

    // get by kurikulum
    @GetMapping("/kurikulum/{idKurikulum}")
    public List<MataKuliahDto> getByKurikulum(@PathVariable Integer idKurikulum) {
        return service.getByKurikulum(idKurikulum);
    }

    // get all approval request
    @GetMapping("/approvalRequest")
    public List<ApprovalRequestList> getAllApproval(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String kurikulum) {
        return service.getAll(search, kurikulum);
    }
}
