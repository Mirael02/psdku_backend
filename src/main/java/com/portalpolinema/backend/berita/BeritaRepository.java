package com.portalpolinema.backend.berita;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BeritaRepository extends JpaRepository<Berita, Integer> {
    Optional<Berita> findBySlug(String slug);
}
