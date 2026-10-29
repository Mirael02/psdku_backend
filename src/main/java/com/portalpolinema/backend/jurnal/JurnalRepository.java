package com.portalpolinema.backend.jurnal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JurnalRepository extends JpaRepository<Jurnal, Integer> {
    Optional<Jurnal> findBySlug(String slug);
}
