package com.portalpolinema.backend.kalender_akademik;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.portalpolinema.backend.approvalRequest.dto.ApprovalRequestList;

@RestController
@RequestMapping("/api/kalender_akademik")
@RequiredArgsConstructor

public class Kalender_AkademikController {
    private final Kalender_AkademikService ps;

    // get all
    @GetMapping
    public List<Kalender_Akademik> getAll() {
        return ps.getAll();
    }

    // get all
    @GetMapping("/approvalRequest")
    public List<ApprovalRequestList> getAll(@RequestParam(required = false) String search) {
        return ps.getAll(search);
    }

}
