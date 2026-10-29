// Backend/src/main/java/com/portalpolinema/backend/pmb/PmbController.java
package com.portalpolinema.backend.pmb;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;

@RestController
@RequestMapping("/api/pmb")
@RequiredArgsConstructor
public class PmbController {
    private final PmbService ps;

    @GetMapping
    public List<Pmb> getAll() {
        return ps.getAll();
    }

    @GetMapping("/approvalRequest")
    public List<ApprovalRequestList> getAll(@RequestParam(required = false) String search) {
        return ps.getAll(search);
    }
}
