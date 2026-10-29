package com.portalpolinema.backend.shared;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DownloadController {
    private final FileStorageService fileStorageService;

    @GetMapping("/api/download")
    public ResponseEntity<Resource> download(
            @RequestParam String url,
            @RequestParam(defaultValue = "false") boolean inline) throws Exception {
        Path path = fileStorageService.resolvePathFromUrl(url);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists())
            return ResponseEntity.notFound().build();

        String contentType = Files.probeContentType(path);
        if (contentType == null)
            contentType = "application/octet-stream";

        String disposition = inline ? "inline" : "attachment";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        disposition + "; filename=\"" + path.getFileName().toString() + "\"")
                .body(resource);
    }

}
