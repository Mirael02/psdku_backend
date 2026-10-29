package com.portalpolinema.backend.mata_kuliah;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MataKuliahRepository extends JpaRepository<MataKuliah, Integer> {
    @Query("SELECT mk FROM MataKuliah mk WHERE mk.kurikulum.id = :idKurikulum")
    List<MataKuliah> findByKurikulumId(@Param("idKurikulum") Integer idKurikulum);
}
