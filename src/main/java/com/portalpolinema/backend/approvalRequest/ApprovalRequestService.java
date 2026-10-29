package com.portalpolinema.backend.approvalRequest;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import com.portalpolinema.backend.approvalRequest.dto.Create;
import com.portalpolinema.backend.berita.Berita;
import com.portalpolinema.backend.berita.BeritaRepository;
import com.portalpolinema.backend.dosen.Dosen;
import com.portalpolinema.backend.dosen.DosenRepository;
import com.portalpolinema.backend.kalender_akademik.Kalender_Akademik;
import com.portalpolinema.backend.kalender_akademik.Kalender_AkademikRepository;
import com.portalpolinema.backend.kurikulum.Kurikulum;
import com.portalpolinema.backend.kurikulum.KurikulumRepository;
import com.portalpolinema.backend.mata_kuliah.MataKuliah;
import com.portalpolinema.backend.mata_kuliah.MataKuliahRepository;
import com.portalpolinema.backend.pengumuman.Pengumuman;
import com.portalpolinema.backend.pengumuman.PengumumanRepository;
import com.portalpolinema.backend.pmb.Pmb;
import com.portalpolinema.backend.pmb.PmbRepository;
import com.portalpolinema.backend.previewimage.PreviewImage;
import com.portalpolinema.backend.previewimage.PreviewImageRepository;
import com.portalpolinema.backend.prodi.Prodi;
import com.portalpolinema.backend.prodi.ProdiRepository;
import com.portalpolinema.backend.shared.FileStorageService;
import com.portalpolinema.backend.shared.PayloadParser;
import com.portalpolinema.backend.user.User;
import com.portalpolinema.backend.user.dto.UserDto;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApprovalRequestService {
    private final ApprovalRequestRepository repo;
    private final PayloadParser payloadParse;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;
    private final FileStorageService fileStorageService;
    private final ProdiRepository ProdiRepo;
    private final PengumumanRepository pengumumanRepo;
    private final PmbRepository pmbRepository;
    private final BeritaRepository beritaRepository;
    private final Kalender_AkademikRepository kalenderAkademikRepository;
    private final KurikulumRepository kurikulumRepository;
    private final MataKuliahRepository mataKuliahRepository;
    private final DosenRepository dosenRepository;
    private final PreviewImageRepository previewImageRepository;

    private boolean isSuperAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_super_admin"));
    }

    public List<ApprovalRequestList> getAll() {
        return repo.findAll().stream()
                .sorted(Comparator.comparingInt(ar -> {
                    return switch (ar.getStatus()) {
                        case Pending -> 0;
                        case Pending_Update -> 1;
                        case Pending_Delete -> 2;
                        case Approved -> 3;
                        case Rejected -> 4;
                    };
                }))
                .map(ar -> new ApprovalRequestList(
                        ar.getId_approval_request(),
                        ar.getNama_tabel(),
                        ar.getTargetId(),
                        ar.getAction().name(),
                        ar.getStatus().name(),
                        payloadParse.parse(ar.getPayload(), ar.getId_approval_request()),
                        new UserDto(ar.getUser().getId_user(), ar.getUser().getNama())))
                .collect(Collectors.toList());
    }

    @Transactional
    public String approve(Integer id, String namaTabel) {
        ApprovalRequest req = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ApprovalRequest not found"));

        if (req.getStatus() == ApprovalRequest.Status.Approved) {
            return "Fitur sudah di approved";
        }

        try {
            Integer targetId = null;

            if ("pengumuman".equalsIgnoreCase(namaTabel)) {
                Pengumuman payload = objectMapper.readValue(req.getPayload(), Pengumuman.class);
                Pengumuman saved = pengumumanRepo.save(payload);
                targetId = saved.getId_pengumuman();

            } else if ("berita".equalsIgnoreCase(namaTabel)) {
                ObjectNode node = (ObjectNode) objectMapper.readTree(req.getPayload());

                String oldFileUrl = node.path("file").asText("");
                String newFileUrl = fileStorageService.finalizeTempUrl(oldFileUrl);
                node.put("file", newFileUrl);

                String oldImgUrl = node.path("gambar").asText("");
                if (oldImgUrl != null && !oldImgUrl.isBlank()) {
                    String newImgUrl = fileStorageService.finalizeTempUrl(oldImgUrl);
                    node.put("gambar", newImgUrl);
                }

                Berita payload = objectMapper.treeToValue(node, Berita.class);
                Berita saved = beritaRepository.save(payload);
                targetId = saved.getId_berita();

                req.setPayload(objectMapper.writeValueAsString(node));

            } else if ("prodi".equalsIgnoreCase(namaTabel)) {
                ObjectNode node = (ObjectNode) objectMapper.readTree(req.getPayload());

                // Pastikan id tidak ikut masuk dari JSON jika nilainya 0 atau null, agar dianggap data baru (Insert)
                if (node.has("id") && (node.get("id").asInt() == 0 || node.get("id").isNull())) {
                    node.remove("id");
                }

                Prodi payload = objectMapper.treeToValue(node, Prodi.class);
                Prodi saved = ProdiRepo.save(payload);
                targetId = saved.getId();

                req.setPayload(objectMapper.writeValueAsString(node));

            } else if ("pmb".equalsIgnoreCase(namaTabel)) {
                ObjectNode node = (ObjectNode) objectMapper.readTree(req.getPayload());

                String oldFileUrl = node.path("file").asText("");
                String newFileUrl = fileStorageService.finalizeTempUrl(oldFileUrl);
                node.put("file", newFileUrl);

                Pmb payload = objectMapper.treeToValue(node, Pmb.class);
                Pmb saved = pmbRepository.save(payload);
                targetId = saved.getId_pmb();

                rebuildPmbPreviewImages(targetId, newFileUrl);
                req.setPayload(objectMapper.writeValueAsString(node));

            } else if ("kalender_akademik".equalsIgnoreCase(namaTabel)) {
                ObjectNode node = (ObjectNode) objectMapper.readTree(req.getPayload());

                String oldFileUrl = node.path("file").asText("");
                String newFileUrl = fileStorageService.finalizeTempUrl(oldFileUrl);
                node.put("file", newFileUrl);

                String oldImgUrl = node.path("gambar").asText("");
                if (oldImgUrl != null && !oldImgUrl.isBlank()) {
                    String newImgUrl = fileStorageService.finalizeTempUrl(oldImgUrl);
                    node.put("gambar", newImgUrl);
                }

                Kalender_Akademik payload = objectMapper.treeToValue(node, Kalender_Akademik.class);
                Kalender_Akademik saved = kalenderAkademikRepository.save(payload);
                targetId = saved.getId_kalender_akademik();

                req.setPayload(objectMapper.writeValueAsString(node));

            } else if ("kurikulum".equalsIgnoreCase(namaTabel)) {
                Kurikulum payload = mapWithProdi(req.getPayload(), Kurikulum.class);
                Kurikulum saved = kurikulumRepository.save(payload);
                targetId = saved.getId();

            } else if ("dosen".equalsIgnoreCase(namaTabel)) {
                Dosen payload = mapWithProdi(req.getPayload(), Dosen.class);
                Dosen saved = dosenRepository.save(payload);
                targetId = saved.getId();

            } else if ("mata_kuliah".equalsIgnoreCase(namaTabel)) {
                MataKuliah payload = mapWithKurikulum(req.getPayload(), MataKuliah.class);
                MataKuliah saved = mataKuliahRepository.save(payload);
                targetId = saved.getId();

            } else {
                throw new RuntimeException("Unknown namaTabel: " + namaTabel);
            }

            req.setTargetId(targetId);
            req.setStatus(ApprovalRequest.Status.Approved);
            repo.save(req);

            return "Fitur berhasil Di Approved";
        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Approve failed for approval_request id: " + id, e);
        }
    }

    @Transactional
    public ApprovalRequest create(Create dto) throws Exception {
        String payloadJson = objectMapper.writeValueAsString(dto.getPayload());
        User userRef = entityManager.getReference(User.class, dto.getId_user());

        ApprovalRequest ar = ApprovalRequest.builder()
                .nama_tabel(dto.getNamaTabel())
                .action(ApprovalRequest.Action.valueOf(dto.getAction()))
                .status(ApprovalRequest.Status.Pending)
                .user(userRef)
                .payload(payloadJson)
                .build();

        ApprovalRequest saved = repo.save(ar);

        // Jika super_admin: langsung approve tanpa perlu menunggu approval
        if (isSuperAdmin()) {
            approve(saved.getId_approval_request(), dto.getNamaTabel());
        }

        return repo.findById(saved.getId_approval_request()).orElse(saved);
    }

    public String update(int id, Create dto) throws Exception {
        ApprovalRequest ar = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ApprovalRequest not found"));

        if ("pmb".equalsIgnoreCase(ar.getNama_tabel())
                && ar.getStatus() == ApprovalRequest.Status.Approved) {
            ar.setStatus(ApprovalRequest.Status.Pending_Update);
            ar.setPayload(objectMapper.writeValueAsString(dto.getPayload()));
            repo.save(ar);
            return "Menunggu persetujuan admin";
        } else if ("kalender_akademik".equalsIgnoreCase(ar.getNama_tabel())
                && ar.getStatus() == ApprovalRequest.Status.Approved) {
            ar.setStatus(ApprovalRequest.Status.Pending_Update);
            ar.setPayload(objectMapper.writeValueAsString(dto.getPayload()));
            repo.save(ar);
            return "Menunggu persetujuan admin";
        } else if ("berita".equalsIgnoreCase(ar.getNama_tabel())
                && ar.getStatus() == ApprovalRequest.Status.Approved) {
            ar.setStatus(ApprovalRequest.Status.Pending_Update);
            ar.setPayload(objectMapper.writeValueAsString(dto.getPayload()));
            repo.save(ar);
            return "Menunggu persetujuan admin";
        } else if ("kurikulum".equalsIgnoreCase(ar.getNama_tabel())
                && ar.getStatus() == ApprovalRequest.Status.Approved) {
            if (ar.getTargetId() == null)
                throw new RuntimeException("TargetId kosong");

            String payloadJson = objectMapper.writeValueAsString(dto.getPayload());
            Kurikulum payload = mapWithProdi(payloadJson, Kurikulum.class);
            payload.setId(ar.getTargetId());
            kurikulumRepository.save(payload);

            ar.setPayload(payloadJson);
            repo.save(ar);

            return "Kondisi Berhasil Di Perbarui";
        } else if ("mata_kuliah".equalsIgnoreCase(ar.getNama_tabel())
                && ar.getStatus() == ApprovalRequest.Status.Approved) {
            if (ar.getTargetId() == null)
                throw new RuntimeException("TargetId kosong");

            String payloadJson = objectMapper.writeValueAsString(dto.getPayload());
            MataKuliah payload = mapWithKurikulum(payloadJson, MataKuliah.class);
            payload.setId(ar.getTargetId());
            mataKuliahRepository.save(payload);

            ar.setPayload(payloadJson);
            repo.save(ar);

            return "Mata Kuliah Berhasil Di Perbarui";
        } else if ("dosen".equalsIgnoreCase(ar.getNama_tabel())
                && ar.getStatus() == ApprovalRequest.Status.Approved) {
            if (ar.getTargetId() == null)
                throw new RuntimeException("TargetId kosong");

            String payloadJson = objectMapper.writeValueAsString(dto.getPayload());
            Dosen payload = mapWithProdi(payloadJson, Dosen.class);
            payload.setId(ar.getTargetId());
            dosenRepository.save(payload);

            ar.setPayload(payloadJson);
            repo.save(ar);

            return "Data Dosen Berhasil Di Perbarui";
        }

        if (ar.getStatus() == ApprovalRequest.Status.Pending
                || ar.getStatus() == ApprovalRequest.Status.Rejected) {
            ar.setNama_tabel(dto.getNamaTabel());
            ar.setAction(ApprovalRequest.Action.valueOf(dto.getAction()));
            ar.setStatus(ApprovalRequest.Status.valueOf(dto.getStatus()));
            ar.setPayload(objectMapper.writeValueAsString(dto.getPayload()));
            repo.save(ar);
            return "Fitur berhasil Di Update";
        }

        return "Fitur hanya bisa di update ketika status Pending Atau Rejected";
    }

    @Transactional
    public String reject(Integer id) {
        ApprovalRequest req = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ApprovalRequest not found"));

        if (req.getStatus() == ApprovalRequest.Status.Approved) {
            return "Data sudah di approved tidak bisa di reject";
        }

        if (req.getStatus() == ApprovalRequest.Status.Pending_Update) {
            if (req.getTargetId() == null)
                throw new RuntimeException("TargetId kosong");

            try {
                String jsonAsli;
                switch (req.getNama_tabel().toLowerCase()) {
                    case "prodi" -> {
                        Prodi data = ProdiRepo.findById(req.getTargetId())
                                .orElseThrow(() -> new RuntimeException("Prodi tidak ditemukan"));
                        jsonAsli = objectMapper.writeValueAsString(data);
                    }
                    case "pengumuman" -> {
                        Pengumuman data = pengumumanRepo.findById(req.getTargetId())
                                .orElseThrow(() -> new RuntimeException("Pengumuman tidak ditemukan"));
                        jsonAsli = objectMapper.writeValueAsString(data);
                    }
                    case "berita" -> {
                        Berita data = beritaRepository.findById(req.getTargetId())
                                .orElseThrow(() -> new RuntimeException("Berita tidak ditemukan"));
                        jsonAsli = objectMapper.writeValueAsString(data);
                    }
                    case "pmb" -> {
                        Pmb data = pmbRepository.findById(req.getTargetId())
                                .orElseThrow(() -> new RuntimeException("PMB tidak ditemukan"));
                        jsonAsli = objectMapper.writeValueAsString(data);
                    }
                    case "kalender_akademik" -> {
                        Kalender_Akademik data = kalenderAkademikRepository.findById(req.getTargetId())
                                .orElseThrow(() -> new RuntimeException("Kalender tidak ditemukan"));
                        jsonAsli = objectMapper.writeValueAsString(data);
                    }
                    case "kurikulum" -> {
                        Kurikulum data = kurikulumRepository.findById(req.getTargetId())
                                .orElseThrow(() -> new RuntimeException("Kurikulum tidak ditemukan"));
                        jsonAsli = objectMapper.writeValueAsString(data);
                    }
                    case "dosen" -> {
                        Dosen data = dosenRepository.findById(req.getTargetId())
                                .orElseThrow(() -> new RuntimeException("Dosen tidak ditemukan"));
                        jsonAsli = objectMapper.writeValueAsString(data);
                    }
                    case "mata_kuliah" -> {
                        MataKuliah data = mataKuliahRepository.findById(req.getTargetId())
                                .orElseThrow(() -> new RuntimeException("Mata Kuliah tidak ditemukan"));
                        jsonAsli = objectMapper.writeValueAsString(data);
                    }
                    default -> throw new RuntimeException("Unknown namaTabel: " + req.getNama_tabel());
                }

                req.setPayload(jsonAsli);
                req.setStatus(ApprovalRequest.Status.Approved);
                repo.save(req);
                return "Data Berhasil Di Rejected dan rollback ke data sebelumnya";
            } catch (Exception e) {
                throw new RuntimeException("Gagal rollback payload", e);
            }
        }

        if (req.getStatus() == ApprovalRequest.Status.Rejected) {
            return "Data sudah di rejected";
        } else if (req.getStatus() == ApprovalRequest.Status.Pending_Delete) {
            req.setStatus(ApprovalRequest.Status.Approved);
            repo.save(req);
            return "Data berhasil Di Rejected";
        } else {
            req.setStatus(ApprovalRequest.Status.Rejected);
            repo.save(req);
            return "Fitur berhasil Di Rejected";
        }
    }

    @Transactional
    public String deleteRequest(int id) {
        ApprovalRequest req = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ApprovalRequest not found"));

        if (req.getStatus() == ApprovalRequest.Status.Approved) {
            req.setStatus(ApprovalRequest.Status.Pending_Delete);
            repo.save(req);
            return "Menunggu Persetujuan Super Admin";
        } else if (req.getStatus() == ApprovalRequest.Status.Pending_Delete && !isSuperAdmin()) {
            return "Data Sedang Di Tinjau Super Admin Tidak Bisa Dihapus";
        } else if (req.getStatus() == ApprovalRequest.Status.Pending_Delete) {
            if ("pengumuman".equalsIgnoreCase(req.getNama_tabel())) {
                if (req.getTargetId() != null)
                    pengumumanRepo.deleteById(req.getTargetId());
            } else if ("berita".equalsIgnoreCase(req.getNama_tabel())) {
                if (req.getTargetId() != null)
                    beritaRepository.deleteById(req.getTargetId());
            } else if ("prodi".equalsIgnoreCase(req.getNama_tabel())) {
                if (req.getTargetId() != null)
                    ProdiRepo.deleteById(req.getTargetId());
            } else if ("pmb".equalsIgnoreCase(req.getNama_tabel())) {
                if (req.getTargetId() != null) {
                    previewImageRepository.deleteByPmbId(req.getTargetId());
                    pmbRepository.deleteById(req.getTargetId());
                }
            } else if ("kalender_akademik".equalsIgnoreCase(req.getNama_tabel())) {
                if (req.getTargetId() != null)
                    kalenderAkademikRepository.deleteById(req.getTargetId());
            } else if ("kurikulum".equalsIgnoreCase(req.getNama_tabel())) {
                if (req.getTargetId() != null)
                    kurikulumRepository.deleteById(req.getTargetId());
            } else if ("dosen".equalsIgnoreCase(req.getNama_tabel())) {
                if (req.getTargetId() != null)
                    dosenRepository.deleteById(req.getTargetId());
            } else if ("mata_kuliah".equalsIgnoreCase(req.getNama_tabel())) {
                if (req.getTargetId() != null)
                    mataKuliahRepository.deleteById(req.getTargetId());
            }

            deleteFilesFromPayload(req.getPayload(), id);

            repo.delete(req);
            return "Data Berhasil Di Hapus";
        } else if (req.getStatus() == ApprovalRequest.Status.Pending
                || req.getStatus() == ApprovalRequest.Status.Rejected) {
            deleteFilesFromPayload(req.getPayload(), id);

            repo.delete(req);
            return "Data Berhasil Di Hapus";
        }

        return "Status tidak diubah";
    }

    private void deleteFilesFromPayload(String payloadJson, int id) {
        try {
            JsonNode node = objectMapper.readTree(payloadJson);

            String gambarUrl = node.hasNonNull("gambarUrl") ? node.get("gambarUrl").asText() : null;
            String fileUrl = node.hasNonNull("file") ? node.get("file").asText() : null;
            String gambar = node.hasNonNull("gambar") ? node.get("gambar").asText() : null;

            if (gambarUrl != null && !gambarUrl.isBlank()) {
                fileStorageService.deleteFileByUrl(gambarUrl);
            }
            if (fileUrl != null && !fileUrl.isBlank()) {
                if (fileUrl.toLowerCase().contains("/pmb/") && fileUrl.toLowerCase().endsWith(".pdf")) {
                    fileStorageService.deletePmbPreviewByPdfUrl(fileUrl);
                }
                fileStorageService.deleteFileByUrl(fileUrl);
            }
            if (gambar != null && !gambar.isBlank()) {
                fileStorageService.deleteFileByUrl(gambar);
            }

        } catch (IOException e) {
            throw new RuntimeException("Gagal hapus file", e);
        } catch (Exception e) {
            throw new RuntimeException("Invalid payload JSON for approval_request id: " + id, e);
        }
    }

    private <T> T mapWithProdi(String payloadJson, Class<T> type) throws Exception {
        ObjectNode node = (ObjectNode) objectMapper.readTree(payloadJson);

        int prodiId = node.has("id")
                ? node.path("id").asInt(0)
                : node.path("prodi").asInt(0);

        if (prodiId == 0)
            throw new RuntimeException("Prodi ID tidak valid");

        // Remove ID and prodi fields to avoid mapping conflict
        node.remove("id");
        node.remove("id_dosen");
        node.remove("prodi");

        Prodi prodi = ProdiRepo.findById(prodiId)
                .orElseThrow(() -> new RuntimeException("Prodi tidak ditemukan: " + prodiId));

        T obj = objectMapper.treeToValue(node, type);

        if (obj instanceof Kurikulum k) {
            k.setProdi(prodi);
        } else if (obj instanceof Dosen d) {
            d.setProdi(prodi);
        }

        return obj;
    }

    private <T> T mapWithKurikulum(String payloadJson, Class<T> type) throws Exception {
        ObjectNode node = (ObjectNode) objectMapper.readTree(payloadJson);

        int kurikulumId = node.has("id")
                ? node.path("id").asInt(0)
                : node.has("id_kurikulum")
                ? node.path("id_kurikulum").asInt(0)
                : node.path("kurikulum").asInt(0);

        if (kurikulumId == 0)
            throw new RuntimeException("Kurikulum ID tidak valid");

        node.remove("id");
        node.remove("id_mk");
        node.remove("id_kurikulum");
        node.remove("kurikulum");

        Kurikulum kurikulum = kurikulumRepository.findById(kurikulumId)
                .orElseThrow(() -> new RuntimeException("Kurikulum tidak ditemukan: " + kurikulumId));

        T obj = objectMapper.treeToValue(node, type);

        if (obj instanceof MataKuliah mk) {
            mk.setKurikulum(kurikulum);
        }

        return obj;
    }

    @Transactional
    public String applyUpdate(Integer id, String namaTabel) {
        ApprovalRequest req = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ApprovalRequest not found"));

        if (req.getStatus() != ApprovalRequest.Status.Pending_Update) {
            return "Status bukan Pending_Update";
        }

        if (req.getTargetId() == null) {
            throw new RuntimeException("TargetId kosong");
        }

        try {
            if ("prodi".equalsIgnoreCase(namaTabel)) {
                ObjectNode node = (ObjectNode) objectMapper.readTree(req.getPayload());

                Prodi payload = objectMapper.treeToValue(node, Prodi.class);
                payload.setId(req.getTargetId());
                ProdiRepo.save(payload);

                req.setPayload(objectMapper.writeValueAsString(node));

            } else if ("pengumuman".equalsIgnoreCase(namaTabel)) {
                Pengumuman payload = objectMapper.readValue(req.getPayload(), Pengumuman.class);
                payload.setId_pengumuman(req.getTargetId());
                pengumumanRepo.save(payload);

            } else if ("kalender_akademik".equalsIgnoreCase(namaTabel)) {
                Kalender_Akademik payload = objectMapper.readValue(req.getPayload(), Kalender_Akademik.class);
                payload.setId_kalender_akademik(req.getTargetId());
                kalenderAkademikRepository.save(payload);

            } else if ("berita".equalsIgnoreCase(namaTabel)) {
                ObjectNode node = (ObjectNode) objectMapper.readTree(req.getPayload());

                String oldFileUrl = node.path("file").asText("");
                if (oldFileUrl != null && !oldFileUrl.isBlank()) {
                    try {
                        String newFileUrl = fileStorageService.finalizeTempUrl(oldFileUrl);
                        node.put("file", newFileUrl);
                    } catch (Exception e) {
                        // ignore
                    }
                }

                Berita payload = objectMapper.treeToValue(node, Berita.class);
                payload.setId_berita(req.getTargetId());
                beritaRepository.save(payload);

                req.setPayload(objectMapper.writeValueAsString(node));

            } else if ("pmb".equalsIgnoreCase(namaTabel)) {
                ObjectNode node = (ObjectNode) objectMapper.readTree(req.getPayload());

                String oldFileUrl = node.path("file").asText("");
                String newFileUrl = fileStorageService.finalizeTempUrl(oldFileUrl);
                node.put("file", newFileUrl);

                Pmb payload = objectMapper.treeToValue(node, Pmb.class);
                payload.setId_pmb(req.getTargetId());
                pmbRepository.save(payload);

                rebuildPmbPreviewImages(req.getTargetId(), newFileUrl);
                req.setPayload(objectMapper.writeValueAsString(node));

            } else {
                throw new RuntimeException("Unknown namaTabel: " + namaTabel);
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid payload JSON", e);
        }

        req.setStatus(ApprovalRequest.Status.Approved);
        repo.save(req);
        return "Update disetujui";
    }

    private void rebuildPmbPreviewImages(Integer idPmb, String pdfUrl) throws IOException {
        if (idPmb == null || pdfUrl == null || pdfUrl.isBlank())
            return;

        previewImageRepository.deleteByPmbId(idPmb);

        List<String> imageUrls = fileStorageService.convertPdfAllPagesToJpgByUrl(pdfUrl);
        if (imageUrls.isEmpty())
            return;

        Pmb pmbRef = entityManager.getReference(Pmb.class, idPmb);

        int page = 1;
        for (String imageUrl : imageUrls) {
            previewImageRepository.save(PreviewImage.builder()
                    .pmb(pmbRef)
                    .image_url(imageUrl)
                    .page_no(page++)
                    .build());
        }
    }
}
