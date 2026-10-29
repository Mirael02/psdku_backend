package com.portalpolinema.backend.mata_kuliah;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.portalpolinema.backend.approvalRequest.ApprovalRequest;
import com.portalpolinema.backend.approvalRequest.ApprovalRequestRepository;
import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import com.portalpolinema.backend.mata_kuliah.dto.MataKuliahDto;
import com.portalpolinema.backend.shared.PayloadParser;
import com.portalpolinema.backend.user.*;
import com.portalpolinema.backend.user.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MataKuliahService {
        private final MataKuliahRepository repo;
        private final ApprovalRequestRepository approvalRepo;
        private final PayloadParser payloadParser;
        private final UserRepository userRepo;

        // get all for guest
        public List<MataKuliahDto> getAll() {
                return repo.findAll().stream()
                                .map(mk -> new MataKuliahDto(
                                                mk.getId(),
                                                mk.getKode(),
                                                mk.getNama(),
                                                mk.getSks_teori(),
                                                mk.getSks_praktikum(),
                                                mk.getJam(),
                                                mk.getJenis().name(),
                                                mk.getKurikulum().getId(),
                                                mk.getKurikulum().getKode(),
                                                mk.getKurikulum().getProdi().getId()))
                                .toList();
        }

        // get all approval request for admin
        public List<ApprovalRequestList> getAll(String search, String kurikulum) {
                List<ApprovalRequest> list = approvalRepo.findAll();

                list = list.stream().filter(ar -> {
                        JsonNode p = payloadParser.parse(ar.getPayload(), ar.getId_approval_request());
                        String nama = p.path("nama").asText("").toLowerCase();
                        String id_kurikulum = p.path("id").asText("").toLowerCase();

                        boolean matchNama = true;
                        boolean matchKurikulum = true;

                        if (search != null && !search.isBlank()) {
                                matchNama = nama.contains(search.toLowerCase());
                        }

                        if (kurikulum != null && !kurikulum.isBlank()) {
                                matchKurikulum = id_kurikulum.contains(kurikulum.toLowerCase());
                        }

                        return matchNama && matchKurikulum;
                }).toList();

                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                User currentUser = userRepo.findByEmail(email).orElseThrow();
                Integer idUser = currentUser.getId_user();

                return list.stream()
                                .filter(ar -> "mata_kuliah".equalsIgnoreCase(ar.getNama_tabel())
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
        public MataKuliahDto getById(Integer id) {
                return repo.findById(id).map(mk -> new MataKuliahDto(
                                mk.getId(),
                                mk.getKode(),
                                mk.getNama(),
                                mk.getSks_teori(),
                                mk.getSks_praktikum(),
                                mk.getJam(),
                                mk.getJenis().name(),
                                mk.getKurikulum().getId(),
                                mk.getKurikulum().getKode(),
                                mk.getKurikulum().getProdi().getId()))
                                .orElseThrow(() -> new RuntimeException("Mata Kuliah not found: " + id));
        }

        // get by kode
        public MataKuliahDto getByKode(String kode) {
                return repo.findAll().stream()
                                .filter(mk -> mk.getKode().equalsIgnoreCase(kode))
                                .findFirst()
                                .map(mk -> new MataKuliahDto(
                                                mk.getId(),
                                                mk.getKode(),
                                                mk.getNama(),
                                                mk.getSks_teori(),
                                                mk.getSks_praktikum(),
                                                mk.getJam(),
                                                mk.getJenis().name(),
                                                mk.getKurikulum().getId(),
                                                mk.getKurikulum().getKode(),
                                                mk.getKurikulum().getProdi().getId()))
                                .orElseThrow(() -> new RuntimeException("Mata Kuliah not found with kode: " + kode));
        }

        // get by kurikulum id
        public List<MataKuliahDto> getByKurikulum(Integer idKurikulum) {
                return repo.findByKurikulumId(idKurikulum).stream()
                                .map(mk -> new MataKuliahDto(
                                                mk.getId(),
                                                mk.getKode(),
                                                mk.getNama(),
                                                mk.getSks_teori(),
                                                mk.getSks_praktikum(),
                                                mk.getJam(),
                                                mk.getJenis().name(),
                                                mk.getKurikulum().getId(),
                                                mk.getKurikulum().getKode(),
                                                mk.getKurikulum().getProdi().getId()))
                                .toList();
        }
}
