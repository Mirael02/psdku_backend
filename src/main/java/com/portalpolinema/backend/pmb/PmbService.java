// Backend/src/main/java/com/portalpolinema/backend/pmb/PmbService.java
package com.portalpolinema.backend.pmb;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.portalpolinema.backend.approvalRequest.ApprovalRequest;
import com.portalpolinema.backend.approvalRequest.ApprovalRequestRepository;
import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import com.portalpolinema.backend.shared.PayloadParser;
import com.portalpolinema.backend.user.dto.UserDto;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PmbService {
    private final PmbRepository pr;
    private final ApprovalRequestRepository repo;
    private final PayloadParser payloadParser;

    public List<Pmb> getAll() {
        return pr.findAllWithPreviewImages();
    }

    public List<ApprovalRequestList> getAll(String search) {
        List<ApprovalRequest> list = repo.findAll();

        if (search != null && !search.isBlank()) {
            String key = search.toLowerCase();
            list = list.stream().filter(ar -> {
                JsonNode p = payloadParser.parse(ar.getPayload(), ar.getId_approval_request());

                String jalur = p.path("jalur_pendaftaran").asText("").toLowerCase();

                String mulai = p.path("tanggal_mulai").asText("");
                String selesai = p.path("tanggal_selesai").asText("");
                String timeline = (mulai + " - " + selesai).toLowerCase();

                return jalur.contains(key) || timeline.contains(key);
            }).toList();
        }

        return list.stream()
                .filter(ar -> "pmb".equalsIgnoreCase(ar.getNama_tabel()))
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
