package com.rental_pg_backend.application.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.rental_pg_backend.application.enums.ApplicationStatusType;
import com.rental_pg_backend.property.entities.Property;
import com.rental_pg_backend.property.entities.PropertyTenantPaymentApplication;
import com.rental_pg_backend.tenant.entities.Tenant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_application_property", columnList = "property_id"),
                @Index(name = "idx_application_tenant", columnList = "tenant_id"),
                @Index(name = "idx_application_status", columnList = "status"),
                @Index(name = "idx_application_start_date", columnList = "startDate")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    @Builder.Default
    private ApplicationStatusType status = ApplicationStatusType.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private Lease lease;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PropertyTenantPaymentApplication propertyTenantPaymentApplication;
}
