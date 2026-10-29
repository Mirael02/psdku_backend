package com.portalpolinema.backend.dashboard;

import com.portalpolinema.backend.berita.BeritaRepository;
import com.portalpolinema.backend.dosen.DosenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DosenRepository dosenRepository;
    private final BeritaRepository beritaRepository;

    public Map<String, Object> getDashboardStats() {
        long totalDosen = dosenRepository.count();
        long totalBerita = beritaRepository.count();

        // Mocking missing tables (Mahasiswa and Attendance)
        long totalMahasiswa = 1245;
        // Mock attendance as ~90% of total dosen, max to totalDosen
        long totalHadir = (long) Math.floor(totalDosen * 0.9);
        if (totalHadir == 0 && totalDosen > 0) {
            totalHadir = totalDosen;
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMahasiswa", totalMahasiswa);
        stats.put("totalDosen", totalDosen);
        stats.put("totalHadir", totalHadir);
        stats.put("totalBerita", totalBerita);

        return stats;
    }
}
