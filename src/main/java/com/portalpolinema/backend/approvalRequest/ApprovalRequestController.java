package com.portalpolinema.backend.approvalRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import com.portalpolinema.backend.approvalRequest.dto.Create;
import com.portalpolinema.backend.kurikulum.Kurikulum;
import com.fasterxml.jackson.databind.JsonNode;
import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/approvalRequest")
@RequiredArgsConstructor

public class ApprovalRequestController {

    private final ApprovalRequestService ars;

    // get all approvalRequest
    @GetMapping
    public List<ApprovalRequestList> getAll() {
        return ars.getAll();
    }

    // create
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Create dto) throws Exception {
        try {
            ApprovalRequest ar = ars.create(dto);

            Map<String, Object> res = new HashMap<>();
            res.put("id", ar.getId_approval_request());
            res.put("namaTabel", ar.getNama_tabel());
            res.put("action", ar.getAction());
            res.put("status", ar.getStatus());
            res.put("payload", ar.getPayload());
            res.put("id_user", dto.getId_user());
            res.put("message", "Data berhasil di tambahkan");
            return ResponseEntity.ok(res);
        } catch (DataIntegrityViolationException e) {
            String msg = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "duplicate data";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", msg));
        }
    }

    // approve
    @PostMapping("/{id}/approve/{namaTabel}")
    public ResponseEntity<Map<String, String>> approve(@PathVariable int id, @PathVariable String namaTabel) {
        try {
            String msg = ars.approve(id, namaTabel);
            return ResponseEntity.ok(Map.of("message", msg));
        } catch (DataIntegrityViolationException e) {
            String msg = e.getMostSpecificCause() != null
                    ? e.getMostSpecificCause().getMessage()
                    : "duplicate data";
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", msg));
        }
    }

    // update
    @PutMapping("/{id}/update")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Create dto) throws Exception {
        try {
            String msg = ars.update(id, dto);
            return ResponseEntity.ok(Map.of("message", msg));
        } catch (DataIntegrityViolationException e) {
            String msg = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "duplicate data";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", msg));
        }
    }

    // jika mau di aktifkan gunanya untuk edit yang sudah ke approve
    @PostMapping("/{id}/approve-update/{namaTabel}")
    public ResponseEntity<Map<String, String>> applyUpdate(@PathVariable int id,
            @PathVariable String namaTabel) {
        try {
            String msg = ars.applyUpdate(id, namaTabel);
            return ResponseEntity.ok(Map.of("message", msg));
        } catch (DataIntegrityViolationException e) {
            String msg = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : "duplicate data";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", msg));
        }
    }

    // reject
    @PutMapping("/{id}/reject")
    public ResponseEntity<Map<String, String>> reject(@PathVariable int id) {
        String msg = ars.reject(id);
        return ResponseEntity.ok(Map.of("message", msg));
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable int id) {
        String msg = ars.deleteRequest(id);
        return ResponseEntity.ok(Map.of("message", msg));
    }

}
