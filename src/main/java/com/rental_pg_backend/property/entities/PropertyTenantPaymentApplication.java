package com.rental_pg_backend.property.entities;

import com.rental_pg_backend.application.entities.Application;
import com.rental_pg_backend.payment.entities.Payment;
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
                @Index(name = "idx_ptpa_tenant", columnList = "tenant_id"),
                @Index(name = "idx_ptpa_property", columnList = "property_id")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyTenantPaymentApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", unique = true)
    private Payment payment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", unique = true)
    private Application application;

}
