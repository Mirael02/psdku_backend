package com.portalpolinema.backend.pmb;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PmbRepository extends JpaRepository<Pmb, Integer> {

    @Query("select distinct p from Pmb p left join fetch p.previewImages")
    List<Pmb> findAllWithPreviewImages();
}
