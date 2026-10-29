package com.portalpolinema.backend.approvalRequest;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "approval_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ApprovalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_approval_request;

    @Column(nullable = false, length = 100)
    private String nama_tabel;

    @Column(nullable = true)
    private Integer targetId;

    public enum Action {
        Create, Update, Delete
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Action action;

    public enum Status {
        Pending, Approved, Pending_Delete, Rejected, Pending_Update
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Status status;

    @Column(columnDefinition = "json", nullable = false)
    private String payload; // JSON string

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private com.portalpolinema.backend.user.User user;

}
