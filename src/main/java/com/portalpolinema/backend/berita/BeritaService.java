package com.portalpolinema.backend.berita;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.portalpolinema.backend.approvalRequest.ApprovalRequest;
import com.portalpolinema.backend.approvalRequest.ApprovalRequestRepository;
import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import com.portalpolinema.backend.shared.*;
import com.portalpolinema.backend.user.*;
import com.portalpolinema.backend.user.dto.UserDto;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeritaService {
    private final ApprovalRequestRepository repo;
    private final BeritaRepository pr;
    private final PayloadParser payloadParser;
    private final UserRepository userRepo;

    public List<Berita> getAll() {
        return pr.findAll();
    }

    public Berita getById(int id) {
        return pr.findById(id)
                .orElseThrow(() -> new RuntimeException("Berita tidak ditemukan"));
    }

    public Berita getBySlug(String slug) {
        return pr.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Berita tidak ditemukan"));
    }

    public int incrementView(int id) {
        Berita berita = getById(id);
        berita.setView(berita.getView() + 1);
        pr.save(berita);
        return berita.getView();
    }

    // get all berita
    public List<ApprovalRequestList> getAllBerita(String search, String date, String category) {
        List<ApprovalRequest> list = repo.findAll();

        list = list.stream().filter(ar -> {
            JsonNode p = payloadParser.parse(ar.getPayload(), ar.getId_approval_request());
            String judul = p.path("judul").asText("").toLowerCase();
            String kategori = p.path("kategori").asText("").toLowerCase();
            String tanggalStr = p.path("tanggal").asText("");

            boolean matchJudul = true;
            boolean matchTanggal = true;
            boolean matchKategori = true;

            if (category != null && !category.isBlank()) {
                matchKategori = kategori.equals(category.toLowerCase());
            }
            if (search != null && !search.isBlank()) {
                matchJudul = judul.contains(search.toLowerCase());
            }

            if (date != null && !date.isBlank()) {
                try {
                    LocalDate filterDate = LocalDate.parse(date);
                    LocalDate tanggal = LocalDate.parse(tanggalStr);
                    matchTanggal = tanggal.equals(filterDate);
                } catch (DateTimeParseException e) {
                    matchTanggal = false;
                }
            }

            return matchJudul && matchTanggal && matchKategori;
        }).toList();

        // filter user + map
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByEmail(email).orElseThrow();
        Integer idUser = currentUser.getId_user();

        return list.stream()
                .filter(ar -> "berita".equalsIgnoreCase(ar.getNama_tabel()) && idUser.equals(ar.getUser().getId_user()))
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
