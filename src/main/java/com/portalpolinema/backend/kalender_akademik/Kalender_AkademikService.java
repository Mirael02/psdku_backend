package com.portalpolinema.backend.kalender_akademik;

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
public class Kalender_AkademikService {
    private final Kalender_AkademikRepository pr;
    private final ApprovalRequestRepository repo;
    private final PayloadParser payloadParser;

    // get all
    public List<Kalender_Akademik> getAll() {
        return pr.findAll();
    }

    // get all
    public List<ApprovalRequestList> getAll(String search) {
        List<ApprovalRequest> list = repo.findAll();

        if (search != null && !search.isBlank()) {
            String key = search.toLowerCase();
            list = list.stream().filter(ar -> {
                JsonNode p = payloadParser.parse(ar.getPayload(), ar.getId_approval_request());
                String tahun_akademik = p.path("tahun_akademik").asText("").toLowerCase();

                return tahun_akademik.contains(key);
            }).toList();
        }

        return list.stream()
                .filter(ar -> "kalender_akademik".equalsIgnoreCase(ar.getNama_tabel()))
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
