package com.scoutli.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "ratings", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_email", "discovery_id" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int score;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "discovery_id", nullable = false)
    private Long discoveryId;
}
