package com.portalpolinema.backend.prodi;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.portalpolinema.backend.approvalRequest.ApprovalRequest;
import com.portalpolinema.backend.approvalRequest.ApprovalRequestRepository;
import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import com.portalpolinema.backend.prodi.dto.ProdiNamaDto;
import com.portalpolinema.backend.shared.PayloadParser;
import com.portalpolinema.backend.user.dto.UserDto;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdiService {
    private final ProdiRepository pr;
    private final ApprovalRequestRepository repo;
    private final PayloadParser payloadParser;

    // get all
    public List<Prodi> getAll() {
        return pr.findAll();
    }

    // get dto
    public List<ProdiNamaDto> getDto() {
        return pr.findAll().stream()
                .map(k -> new ProdiNamaDto(
                        k.getId(),
                        k.getNama()))
                .toList();
    }

    // get all prodi
    public List<ApprovalRequestList> getAllProdi(String search) {
        List<ApprovalRequest> list = repo.findAll();

        if (search != null && !search.isBlank()) {
            String key = search.toLowerCase();
            list = list.stream().filter(ar -> {
                JsonNode p = payloadParser.parse(ar.getPayload(), ar.getId_approval_request());
                String kode = p.path("kode").asText("").toLowerCase();
                String nama = p.path("nama").asText("").toLowerCase();
                return kode.contains(key) || nama.contains(key);
            }).toList();
        }

        return list.stream()
                .filter(ar -> "prodi".equalsIgnoreCase(ar.getNama_tabel()))
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
