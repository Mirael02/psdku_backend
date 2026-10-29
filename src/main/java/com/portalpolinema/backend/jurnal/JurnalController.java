package com.portalpolinema.backend.jurnal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jurnal")
@RequiredArgsConstructor
public class JurnalController {
    private final JurnalService jurnalService;

    @GetMapping
    public List<Jurnal> getAll() {
        return jurnalService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jurnal> getById(@PathVariable int id) {
        return jurnalService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Jurnal> getBySlug(@PathVariable String slug) {
        return jurnalService.getBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Jurnal create(@RequestBody Jurnal jurnal) {
        return jurnalService.save(jurnal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jurnal> update(@PathVariable int id, @RequestBody Jurnal jurnal) {
        try {
            return ResponseEntity.ok(jurnalService.update(id, jurnal));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        jurnalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
