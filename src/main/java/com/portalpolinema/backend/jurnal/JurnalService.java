package com.portalpolinema.backend.jurnal;

import com.portalpolinema.backend.shared.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JurnalService {
    private final JurnalRepository jurnalRepository;

    public List<Jurnal> getAll() {
        return jurnalRepository.findAll();
    }

    public Optional<Jurnal> getById(int id) {
        return jurnalRepository.findById(id);
    }

    public Optional<Jurnal> getBySlug(String slug) {
        return jurnalRepository.findBySlug(slug);
    }

    public Jurnal save(Jurnal jurnal) {
        if (jurnal.getSlug() == null || jurnal.getSlug().isEmpty()) {
            jurnal.setSlug(SlugUtil.slugifyFinal(jurnal.getJudul()));
        }
        return jurnalRepository.save(jurnal);
    }

    public Jurnal update(int id, Jurnal jurnalDetails) {
        Jurnal jurnal = jurnalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jurnal not found with id: " + id));
        
        jurnal.setJudul(jurnalDetails.getJudul());
        jurnal.setSlug(SlugUtil.slugifyFinal(jurnalDetails.getJudul()));
        jurnal.setGambar(jurnalDetails.getGambar());
        jurnal.setLink(jurnalDetails.getLink());
        
        return jurnalRepository.save(jurnal);
    }

    public void delete(int id) {
        jurnalRepository.deleteById(id);
    }
}
