package com.portalpolinema.backend.approvalRequest.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.portalpolinema.backend.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequestList {
    private Integer id_approval_request;
    private String nama_tabel;
    private Integer targetId;
    private String action;
    private String status;
    private JsonNode payload;
    private UserDto user;
}
