package com.portalpolinema.backend.pengumuman;

import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;

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
@RequestMapping("/api/pengumuman")
@RequiredArgsConstructor
public class PengumumanController {

    private final PengumumanService ps;
    
    @GetMapping
    public List<Pengumuman> getAll() {
        return ps.getAll();
    }

    @GetMapping("/{id}")
    public Pengumuman getById(@PathVariable int id) {
        return ps.getById(id);
    }

    @PostMapping("/{id}/view")
    public Map<String, Integer> incrementView(@PathVariable int id) {
        return Map.of("view", ps.incrementView(id));
    }

    @GetMapping("/approvalRequest")
    public List<ApprovalRequestList> getAllBerita(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String category) {
        return ps.getAllPengumuman(search, date, category);
    }
}
