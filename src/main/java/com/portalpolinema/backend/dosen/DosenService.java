package com.portalpolinema.backend.dosen;

import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.portalpolinema.backend.approvalRequest.ApprovalRequest;
import com.portalpolinema.backend.approvalRequest.ApprovalRequestRepository;
import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import com.portalpolinema.backend.dosen.dto.DosenDto;
import com.portalpolinema.backend.prodi.dto.ProdiNamaDto;
import com.portalpolinema.backend.shared.PayloadParser;
import com.portalpolinema.backend.user.*;
import com.portalpolinema.backend.user.dto.UserDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class DosenService {
    private final DosenRepository pr;
    private final ApprovalRequestRepository repo;
    private final PayloadParser payloadParser;
    private final UserRepository userRepo;

    // get all
    public List<DosenDto> getAll() {
        return pr.findAll().stream()
                .map(k -> new DosenDto(
                        k.getId(),
                        k.getNama(),
                        k.getNip(),
                        k.getJabatan_fungsional(),
                        k.getIsActive(),
                        new ProdiNamaDto(
                                k.getProdi().getId(),
                                k.getProdi().getNama())))
                .toList();
    }

    // get all for admin
    public List<ApprovalRequestList> getAll(String search, String prodi) {
        List<ApprovalRequest> list = repo.findAll();

        list = list.stream().filter(ar -> {
            JsonNode p = payloadParser.parse(ar.getPayload(), ar.getId_approval_request());
            String nama = p.path("nama").asText("").toLowerCase();
            String nip = p.path("nip").asText("").toLowerCase();
            String id_prodi = p.path("id_prodi").asText("").toLowerCase();

            boolean matchSearch = true;
            boolean matchProdi = true;

            if (search != null && !search.isBlank()) {
                String s = search.toLowerCase();
                matchSearch = nama.contains(s) || nip.contains(s);
            }

            if (prodi != null && !prodi.isBlank()) {
                matchProdi = id_prodi.contains(prodi.toLowerCase());
            }

            return matchSearch && matchProdi;
        }).toList();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByEmail(email).orElseThrow();
        Integer idUser = currentUser.getId_user();

        return list.stream()
                .filter(ar -> "dosen".equalsIgnoreCase(ar.getNama_tabel())
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
}