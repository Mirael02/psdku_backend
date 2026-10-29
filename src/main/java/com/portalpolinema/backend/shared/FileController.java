package com.portalpolinema.backend.shared;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.*;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/uploads")
public class FileController {

    private final FileStorageService storage;

    public FileController(FileStorageService storage) {
        this.storage = storage;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestPart("baseName") String baseName,
            @RequestPart("dirUploads") String dirUploads,
            @RequestPart("file") MultipartFile file) throws IOException {

        boolean isKalender = "kalender_akademik".equalsIgnoreCase(dirUploads);
        boolean isPdf = file.getOriginalFilename() != null
                && file.getOriginalFilename().toLowerCase().endsWith(".pdf");

        // Validasi khusus kalender akademik: PDF max 1 halaman
        if (isKalender && isPdf) {
            int totalPages = storage.countPdfPages(file);
            if (totalPages > 1) {
                return ResponseEntity.badRequest()
                        .body(new FileUploadResponse(null, null, "Maksimal 1 halaman"));
            }
        }

        String fileUrl = storage.saveFoto(baseName, dirUploads, file);
        String slug = null;

        if (isKalender && fileUrl.toLowerCase().endsWith(".pdf")) {
            slug = storage.convertPdfFirstPageToJpgByUrl(fileUrl);
        }

        if ("berita".equalsIgnoreCase(dirUploads)) {
            slug = SlugUtil.slugifyFinal(baseName);
        }

        return ResponseEntity.ok(new FileUploadResponse(fileUrl, slug, "Data berhasil di simpan"));
    }

    @PostMapping("/rename")
    public ResponseEntity<FileUploadResponse> renameFile(
            @RequestParam("fileUrl") String fileUrl,
            @RequestParam("baseName") String baseName) throws IOException {

        String newFileUrl = storage.renameFileByUrl(fileUrl, baseName);
        String slug = storage.renameCalendarPreviewOnRename(fileUrl, newFileUrl);

        if (slug == null && newFileUrl.toLowerCase().contains("/kalender_akademik/")
                && newFileUrl.toLowerCase().endsWith(".pdf")) {
            slug = storage.convertPdfFirstPageToJpgByUrl(newFileUrl);
        }

        if (newFileUrl.toLowerCase().contains("/berita/")) {
            slug = SlugUtil.slugifyFinal(baseName);
        }

        return ResponseEntity.ok(new FileUploadResponse(newFileUrl, slug, "Data berhasil di simpan"));
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteFile(
            @RequestParam("fileUrl") String fileUrl) throws IOException {

        storage.deleteFileByUrl(fileUrl);
        storage.deleteCalendarPreviewByPdfUrl(fileUrl);
        storage.deletePmbPreviewByPdfUrl(fileUrl); // tambahan
        return ResponseEntity.ok(Map.of("message", "File deleted"));
    }

}

@Getter
@Setter
@AllArgsConstructor
class FileUploadResponse {
    private String url;
    private String slug;
    private String message;
}
