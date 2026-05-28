package com.rental_pg_backend.application.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_lease_application", columnList = "application_id"),
                @Index(name = "idx_lease_end_date", columnList = "endDate")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lease {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @Builder.Default
    private Double rent = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Double deposit = 0.0;

    @OneToOne
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private Application application;

}
