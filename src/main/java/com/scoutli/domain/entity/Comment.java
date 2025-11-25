package com.scoutli.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "discovery_id", nullable = false)
    private Long discoveryId;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
