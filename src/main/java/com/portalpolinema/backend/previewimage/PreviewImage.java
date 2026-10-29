package com.portalpolinema.backend.previewimage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portalpolinema.backend.pmb.Pmb;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "preview_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_preview_image")
    private Integer id_preview_image;

    @Column(name = "image_url", length = 500, nullable = false)
    private String image_url;

    @Column(name = "page_no", nullable = false)
    private Integer page_no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pmb", nullable = false)
    @JsonIgnore
    private Pmb pmb;
}
