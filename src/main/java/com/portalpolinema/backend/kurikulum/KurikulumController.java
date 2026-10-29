package com.portalpolinema.backend.kurikulum;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import com.portalpolinema.backend.kurikulum.dto.KurikulumDto;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/kurikulum")
@RequiredArgsConstructor

public class KurikulumController {
    private final KurikulumService ks;

    // get all for guest
    @GetMapping("/guest")
    public List<KurikulumDto> getAllGuest() {
        return ks.getAll();
    }

    // get by id
    @GetMapping("/{id}")
    public KurikulumDto getById(@PathVariable Integer id) {
        return ks.getById(id);
    }

    // get by name
    @GetMapping("/name/{nama}")
    public KurikulumDto getByNama(@PathVariable String nama) {
        return ks.getByNama(nama);
    }

    // get all approval request
    @GetMapping("/approvalRequest")
    public List<ApprovalRequestList> getAllApproval(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String prodi,
            @RequestParam(required = false) String kondisi) {
        return ks.getAll(search, prodi, kondisi);
    }

}
