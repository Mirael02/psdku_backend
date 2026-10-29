package com.portalpolinema.backend.shared;

import org.springframework.beans.factory.annotation.Value;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {

    @Value("${dir-prodi}")
    private String dir_prodi;

    @Value("${dir-pengumuman}")
    private String dir_pengumuman;

    @Value("${dir-berita}")
    private String dir_berita;

    @Value("${dir-pmb}")
    private String dir_pmb;

    @Value("${dir-kalender_akademik}")
    private String dir_kalender_akademik;

    @Value("${dir-kurikulum}")
    private String dir_kurikulum;

    @Value("${dir-dosen}")
    private String dir_dosen;

    public String saveFoto(String baseName, String dirUploads, MultipartFile file) throws IOException {
        Path dir;
        if ("prodi".equalsIgnoreCase(dirUploads)) {
            dir = Paths.get(dir_prodi);
        } else if ("pengumuman".equalsIgnoreCase(dirUploads)) {
            dir = Paths.get(dir_pengumuman);
        } else if ("berita".equalsIgnoreCase(dirUploads)) {
            dir = Paths.get(dir_berita);
        } else if ("pmb".equalsIgnoreCase(dirUploads)) {
            dir = Paths.get(dir_pmb);
        } else if ("kalender_akademik".equalsIgnoreCase(dirUploads)) {
            dir = Paths.get(dir_kalender_akademik);
        } else if ("kurikulum".equalsIgnoreCase(dirUploads)) {
            dir = Paths.get(dir_kurikulum);
        } else if ("dosen".equalsIgnoreCase(dirUploads)) {
            dir = Paths.get(dir_dosen);
        } else {
            throw new IllegalArgumentException("Unknown dirUploads: " + dirUploads);
        }

        String safe = SlugUtil.slugifyTemp(baseName);

        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        }

        String filename = safe + ext;
        Path target = dir.resolve(filename);

        // Ensure upload directory exists before writing the file.
        Files.createDirectories(dir);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + dirUploads + "/" + filename;
    }

    public void deleteFileByUrl(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isBlank())
            return;

        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

        Path dir;
        if (fileUrl.contains("/prodi/")) {
            dir = Paths.get(dir_prodi);
        } else if (fileUrl.contains("/pengumuman/")) {
            dir = Paths.get(dir_pengumuman);
        } else if (fileUrl.contains("/berita/")) {
            dir = Paths.get(dir_berita);
        } else if (fileUrl.contains("/pmb/")) {
            dir = Paths.get(dir_pmb);
        } else if (fileUrl.contains("/kalender_akademik/")) {
            dir = Paths.get(dir_kalender_akademik);
        } else if (fileUrl.contains("/kurikulum/")) {
            dir = Paths.get(dir_kurikulum);
        } else if (fileUrl.contains("/dosen/")) {
            dir = Paths.get(dir_dosen);
        } else {
            throw new IllegalArgumentException("Unknown fileUrl: " + fileUrl);
        }

        Path target = dir.resolve(fileName);
        Files.deleteIfExists(target);
    }

    public String renameFileByUrl(String fileUrl, String newBaseName) throws IOException {
        if (fileUrl == null || fileUrl.isBlank())
            return fileUrl;

        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

        Path dir;
        String dirName;
        if (fileUrl.contains("/prodi/")) {
            dir = Paths.get(dir_prodi);
            dirName = "prodi";
        } else if (fileUrl.contains("/pengumuman/")) {
            dir = Paths.get(dir_pengumuman);
            dirName = "pengumuman";
        } else if (fileUrl.contains("/berita/")) {
            dir = Paths.get(dir_berita);
            dirName = "berita";
        } else if (fileUrl.contains("/pmb/")) {
            dir = Paths.get(dir_pmb);
            dirName = "pmb";
        } else if (fileUrl.contains("/kalender_akademik/")) {
            dir = Paths.get(dir_kalender_akademik);
            dirName = "kalender_akademik";
        } else if (fileUrl.contains("/kurikulum/")) {
            dir = Paths.get(dir_kurikulum);
            dirName = "kurikulum";
        } else if (fileUrl.contains("/dosen/")) {
            dir = Paths.get(dir_dosen);
            dirName = "dosen";
        } else {
            throw new IllegalArgumentException("Unknown fileUrl: " + fileUrl);
        }

        String ext = "";
        if (fileName.contains("."))
            ext = fileName.substring(fileName.lastIndexOf("."));

        String safe = SlugUtil.slugifyTemp(newBaseName);

        String newName = safe + ext;

        Path source = dir.resolve(fileName);
        Path target = dir.resolve(newName);

        if (Files.exists(source)) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/uploads/" + dirName + "/" + newName;
    }

    public String renameFileByUrlTemp(String fileUrl, String newBaseName) throws IOException {
        if (fileUrl == null || fileUrl.isBlank())
            return fileUrl;

        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

        Path dir;
        String dirName;
        if (fileUrl.contains("/prodi/")) {
            dir = Paths.get(dir_prodi);
            dirName = "prodi";
        } else if (fileUrl.contains("/pengumuman/")) {
            dir = Paths.get(dir_pengumuman);
            dirName = "pengumuman";
        } else if (fileUrl.contains("/berita/")) {
            dir = Paths.get(dir_berita);
            dirName = "berita";
        } else if (fileUrl.contains("/pmb/")) {
            dir = Paths.get(dir_pmb);
            dirName = "pmb";
        } else if (fileUrl.contains("/kalender_akademik/")) {
            dir = Paths.get(dir_kalender_akademik);
            dirName = "kalender_akademik";
        } else if (fileUrl.contains("/kurikulum/")) {
            dir = Paths.get(dir_kurikulum);
            dirName = "kurikulum";
        } else if (fileUrl.contains("/dosen/")) {
            dir = Paths.get(dir_dosen);
            dirName = "dosen";
        } else {
            throw new IllegalArgumentException("Unknown fileUrl: " + fileUrl);
        }

        String ext = "";
        if (fileName.contains("."))
            ext = fileName.substring(fileName.lastIndexOf("."));

        String safe = SlugUtil.slugifyFinal(newBaseName);

        String newName = safe + ext;

        Path source = dir.resolve(fileName);
        Path target = dir.resolve(newName);

        if (Files.exists(source)) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/uploads/" + dirName + "/" + newName;
    }

    public String finalizeTempUrl(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isBlank())
            return fileUrl;

        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1).trim();
        String name = fileName;
        if (fileName.contains(".")) {
            name = fileName.substring(0, fileName.lastIndexOf("."));
        }

        String newBase = name.replaceFirst("(?i)_temp$", "");
        if (newBase.equals(name))
            return fileUrl;

        return renameFileByUrlTemp(fileUrl, newBase);
    }

    public Path resolvePathFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank())
            throw new IllegalArgumentException("fileUrl kosong");

        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

        Path dir;
        if (fileUrl.contains("/prodi/")) {
            dir = Paths.get(dir_prodi);
        } else if (fileUrl.contains("/pengumuman/")) {
            dir = Paths.get(dir_pengumuman);
        } else if (fileUrl.contains("/berita/")) {
            dir = Paths.get(dir_berita);
        } else if (fileUrl.contains("/pmb/")) {
            dir = Paths.get(dir_pmb);
        } else if (fileUrl.contains("/kalender_akademik/")) {
            dir = Paths.get(dir_kalender_akademik);
        } else if (fileUrl.contains("/kurikulum/")) {
            dir = Paths.get(dir_kurikulum);
        } else if (fileUrl.contains("/dosen/")) {
            dir = Paths.get(dir_dosen);
        } else {
            throw new IllegalArgumentException("Unknown fileUrl: " + fileUrl);
        }

        return dir.resolve(fileName);
    }

    // Tambahan: hitung jumlah halaman PDF dari file upload
    public int countPdfPages(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            return document.getNumberOfPages();
        }
    }

    public String convertPdfFirstPageToJpgByUrl(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isBlank())
            return null;
        if (!fileUrl.toLowerCase().contains("/kalender_akademik/"))
            return null;
        if (!fileUrl.toLowerCase().endsWith(".pdf"))
            return null;

        Path pdfPath = resolvePathFromUrl(fileUrl);
        if (!Files.exists(pdfPath))
            return null;

        String fileName = pdfPath.getFileName().toString();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        Path jpgPath = pdfPath.getParent().resolve(baseName + ".jpg");

        try (PDDocument document = PDDocument.load(pdfPath.toFile())) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, 150);
            ImageIO.write(image, "jpg", jpgPath.toFile());
        }

        return "/uploads/" + "kalender_akademik/" + baseName + ".jpg";
    }

    public String renameCalendarPreviewOnRename(String oldPdfUrl, String newPdfUrl) throws IOException {
        if (oldPdfUrl == null || oldPdfUrl.isBlank())
            return null;
        if (newPdfUrl == null || newPdfUrl.isBlank())
            return null;
        if (!oldPdfUrl.toLowerCase().contains("/kalender_akademik/"))
            return null;
        if (!oldPdfUrl.toLowerCase().endsWith(".pdf"))
            return null;
        if (!newPdfUrl.toLowerCase().contains("/kalender_akademik/"))
            return null;
        if (!newPdfUrl.toLowerCase().endsWith(".pdf"))
            return null;

        Path oldPdfPath = resolvePathFromUrl(oldPdfUrl);
        Path newPdfPath = resolvePathFromUrl(newPdfUrl);

        String oldBase = oldPdfPath.getFileName().toString().replaceFirst("\\.[^.]+$", "");
        String newBase = newPdfPath.getFileName().toString().replaceFirst("\\.[^.]+$", "");

        Path oldJpg = oldPdfPath.getParent().resolve(oldBase + ".jpg");
        Path newJpg = newPdfPath.getParent().resolve(newBase + ".jpg");

        if (Files.exists(oldJpg)) {
            Files.move(oldJpg, newJpg, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + "kalender_akademik/" + newBase + ".jpg";
        }

        return null;
    }

    public void deleteCalendarPreviewByPdfUrl(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isBlank())
            return;
        if (!fileUrl.toLowerCase().contains("/kalender_akademik/"))
            return;
        if (!fileUrl.toLowerCase().endsWith(".pdf"))
            return;

        Path pdfPath = resolvePathFromUrl(fileUrl);
        String base = pdfPath.getFileName().toString().replaceFirst("\\.[^.]+$", "");
        Path jpgPath = pdfPath.getParent().resolve(base + ".jpg");
        Files.deleteIfExists(jpgPath);
    }

    public List<String> convertPdfAllPagesToJpgByUrl(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isBlank())
            return List.of();
        if (!fileUrl.toLowerCase().contains("/pmb/"))
            return List.of();
        if (!fileUrl.toLowerCase().endsWith(".pdf"))
            return List.of();

        Path pdfPath = resolvePathFromUrl(fileUrl);
        if (!Files.exists(pdfPath))
            return List.of();

        String fileName = pdfPath.getFileName().toString();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));

        List<String> imageUrls = new ArrayList<>();

        try (PDDocument document = PDDocument.load(pdfPath.toFile())) {
            PDFRenderer renderer = new PDFRenderer(document);
            int totalPages = document.getNumberOfPages();

            for (int i = 0; i < totalPages; i++) {
                String outName = baseName + "_" + (i + 1) + ".jpg";
                Path outPath = pdfPath.getParent().resolve(outName);

                BufferedImage image = renderer.renderImageWithDPI(i, 150);
                ImageIO.write(image, "jpg", outPath.toFile());

                imageUrls.add("/uploads/" + "pmb/" + outName);
            }
        }

        return imageUrls;
    }

    public void deletePmbPreviewByPdfUrl(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isBlank())
            return;
        if (!fileUrl.toLowerCase().contains("/pmb/"))
            return;
        if (!fileUrl.toLowerCase().endsWith(".pdf"))
            return;

        Path pdfPath = resolvePathFromUrl(fileUrl);
        String base = pdfPath.getFileName().toString().replaceFirst("\\.[^.]+$", "");

        int page = 1;
        while (true) {
            Path img = pdfPath.getParent().resolve(base + "_" + page + ".jpg");
            if (!Files.exists(img))
                break;
            Files.deleteIfExists(img);
            page++;
        }
    }

}
