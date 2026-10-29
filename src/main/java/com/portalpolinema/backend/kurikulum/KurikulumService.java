package com.portalpolinema.backend.kurikulum;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.portalpolinema.backend.approvalRequest.ApprovalRequest;
import com.portalpolinema.backend.approvalRequest.ApprovalRequestRepository;
import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import com.portalpolinema.backend.kurikulum.dto.KurikulumDto;
import com.portalpolinema.backend.prodi.dto.ProdiNamaDto;
import com.portalpolinema.backend.shared.PayloadParser;
import com.portalpolinema.backend.user.*;
import com.portalpolinema.backend.user.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KurikulumService {
    private final KurikulumRepository pr;
    private final ApprovalRequestRepository repo;
    private final PayloadParser payloadParser;
    private final UserRepository userRepo;

    // get all
    public List<KurikulumDto> getAll() {
        return pr.findAll().stream()
                .map(k -> new KurikulumDto(
                        k.getId(),
                        k.getKode(),
                        k.getTahun_berlaku(),
                        k.getStatus(),
                        new ProdiNamaDto(
                                k.getProdi().getId(),
                                k.getProdi().getNama())))
                .toList();
    }

    // get all
    public List<ApprovalRequestList> getAll(String search, String prodi, String kondisi) {
        List<ApprovalRequest> list = repo.findAll();

        list = list.stream().filter(ar -> {
            JsonNode p = payloadParser.parse(ar.getPayload(), ar.getId_approval_request());
            String kode = p.path("kode").asText("").toLowerCase();
            String id_prodi = p.path("id").asText("").toLowerCase();
            String status = p.path("status").asText("status").toLowerCase();

            boolean matchnama = true;
            boolean matchprodi = true;
            boolean matchkondisi = true;

            if (search != null && !search.isBlank()) {
                matchnama = kode.contains(search.toLowerCase());
            }

            if (prodi != null && !prodi.isBlank()) {
                matchprodi = id_prodi.contains(prodi.toLowerCase());
            }

            if (kondisi != null && !kondisi.isBlank()) {
                matchkondisi = Kurikulum.Status.valueOf(kondisi).name()
                        .equalsIgnoreCase(status);
            }

            return matchnama && matchprodi && matchkondisi;
        }).toList();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByEmail(email).orElseThrow();
        Integer idUser = currentUser.getId_user();

        return list.stream()
                .filter(ar -> "kurikulum".equalsIgnoreCase(ar.getNama_tabel())
                        && idUser.equals(ar.getUser().getId_user()))
                .map(ar -> new ApprovalRequestList(
                        ar.getId_approval_request(),
                        ar.getNama_tabel(),
                        ar.getTargetId(),
                        ar.getAction().name(),
                        ar.getStatus().name(),
                        payloadParser.parse(ar.getPayload(), ar.getId_approval_request()),
                        new UserDto(ar.getUser().getId_user(), ar.getUser().getNama())))
                .toList();
    }

    // get by id
    public KurikulumDto getById(Integer id) {
        return pr.findById(id).map(k -> new KurikulumDto(
                k.getId(),
                k.getKode(),
                k.getTahun_berlaku(),
                k.getStatus(),
                new ProdiNamaDto(
                        k.getProdi().getId(),
                        k.getProdi().getNama())))
                .orElseThrow(() -> new RuntimeException("Kurikulum not found: " + id));
    }

    // get by name
    public KurikulumDto getByNama(String nama) {
        return pr.findAll().stream()
                .filter(k -> k.getKode().equalsIgnoreCase(nama))
                .findFirst()
                .map(k -> new KurikulumDto(
                        k.getId(),
                        k.getKode(),
                        k.getTahun_berlaku(),
                        k.getStatus(),
                        new ProdiNamaDto(
                                k.getProdi().getId(),
                                k.getProdi().getNama())))
                .orElseThrow(() -> new RuntimeException("Kurikulum not found with name: " + nama));
    }
}
