package com.portalpolinema.backend.berita;

import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import com.portalpolinema.backend.pmb.Pmb;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/berita")
@RequiredArgsConstructor
public class BeritaController {

    private final BeritaService bs;

    @GetMapping
    public List<Berita> getAll() {
        return bs.getAll();
    }

    @GetMapping("/id/{id}")
    public Berita getById(@PathVariable int id) {
        return bs.getById(id);
    }

    @GetMapping("/slug/{slug}")
    public Berita getBySlug(@PathVariable String slug) {
        return bs.getBySlug(slug);
    }

    @PostMapping("/{id}/view")
    public Map<String, Integer> incrementView(@PathVariable int id) {
        return Map.of("view", bs.incrementView(id));
    }

    @GetMapping("/approvalRequest")
    public List<ApprovalRequestList> getAllBerita(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String category) {
        return bs.getAllBerita(search, date, category);
    }
}
