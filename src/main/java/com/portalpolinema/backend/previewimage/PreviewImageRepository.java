package com.portalpolinema.backend.previewimage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PreviewImageRepository extends JpaRepository<PreviewImage, Integer> {

    @Modifying
    @Transactional
    @Query("delete from PreviewImage pi where pi.pmb.id_pmb = :idPmb")
    void deleteByPmbId(@Param("idPmb") Integer idPmb);
}
