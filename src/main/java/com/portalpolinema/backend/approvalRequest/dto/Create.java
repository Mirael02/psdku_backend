package com.portalpolinema.backend.approvalRequest.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Create {
    private String namaTabel;
    private String action;
    private String status;
    private Integer id_user;
    private Object payload;
}